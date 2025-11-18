package service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.*;
import util.FileManager;

public class TaskService {

    private HR hr; //Needs HR to access staff database
    private Scanner sc;
    private String filename;

    private List<Task> allTasks = new ArrayList<>();

    public TaskService (HR hr, Scanner sc, String f) {

        this.hr = hr;
        this.sc = sc;
        this.filename = f;
        loadTasks();

    }

    //View all tasks
    //For Staff: Assigned tasks, For Managers: Created tasks
    public void viewTasks(Staff s) {

        System.out.println("\n--- TASK LIST ---");
        List <Task> taskList = s.getTasks();
        if (taskList.size() == 0) System.out.println("There are no tasks.");

        for (int i=1; i<=taskList.size(); i++) {

            Task currTask = taskList.get(i-1);
            System.out.print(i + ". " + currTask.getName());

            //If manager, show name assigned employee if applicable
            if (s instanceof Manager) {
                if (currTask.isAssigned()) System.out.print(" (Assigned to " + currTask.getAssignee().getName() + "): ");
                else System.out.print(": ");
            }
            //If not manager, show name of manager who assigned the task + due date
            else {
                System.out.print(" (Assigned by " + currTask.getCreator().getName() + ", Due " + currTask.getDeadline() + "): ");
            }

            //Print status of each task
            if (currTask.isCompleted()) {

                System.out.print("COMPLETED ");
                //Print on time status
                if (currTask.onTime()) System.out.println("ON TIME");
                else System.out.println("LATE");
            }
            else if (!currTask.isAssigned()) System.out.println("UNASSIGNED");
            else if (!currTask.isAccepted()) System.out.println("AWAITING RESPONSE");
            else System.out.println("IN PROGRESS");
        }

    }

    //Manager creates task
    public Task createTask (Manager creator) {

        sc.nextLine();
        System.out.println("Enter task name:");
        String name = sc.nextLine();
        System.out.println("Enter description of task:");
        String desc = sc.nextLine();

        //Get date and time for deadline
        System.out.println("Enter deadline date (YYYY-MM-DD):");
        String dateText = sc.nextLine();
        System.out.println("Enter deadline time (HH:MM) (24 hour time):");
        String timeText = sc.nextLine();

        //Get date time object from text input
        LocalDateTime deadline = dateStringToObject(dateText, timeText);

        Task newTask = new Task(creator, name, desc, deadline);

        allTasks.add(newTask);
        creator.addTask(newTask);
        saveTasks();

        int choice = 0;
        do {
            //Success message
            System.out.println("Successfully created task.");
            //Next steps
            System.out.println("\nWhat do you want to do now?");
            System.out.println("1. Assign Task to Employee");
            System.out.println("2. Save for Later");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> assignTask(newTask, creator);
                case 2 -> System.out.println("Returning...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice <= 0);

        return newTask;

    }

    //Manager begins the task assignment menu
    public void beginAssignment (Manager creator) {

        List<Task> taskList = creator.getUnassignedTasks();

        int choice = -1;
        //Begin selection of task to assign
        do {
            System.out.println("\nSelect task to assign:");
            
            for (int i=1; i<=taskList.size(); i++) {
                System.out.println(i + ". " + taskList.get(i-1).getName() + ": UNASSIGNED");
            }

            System.out.println("0: Cancel");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            //cancel or assign task to employee
            if (choice == 0) System.out.println("Returning...");
            else if (choice < taskList.size()+1) {
                Task task = taskList.get(choice-1);
                assignTask(task, creator);
            }
            else System.out.println("Invalid option.");

        } while (choice < 0);

    }

    //Manager assigns task to employee
    private void assignTask (Task task, Manager creator) {

        int choice = -1;
        do {
            System.out.println("\nWho would you like to assign this task to?");

            List<Staff> employeeList = creator.getAvailableEmployees();
            for (int i=1; i<=employeeList.size(); i++) {
                System.out.println(i + ". " + employeeList.get(i-1).getName() + ": AVAILABLE");
            }

            System.out.println("0: Cancel");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            //cancel or assign task to employee
            if (choice == 0) System.out.println("Returning...");
            else if (choice < employeeList.size()+1) {
                task.assign(employeeList.get(choice-1));
                //Success message
                System.out.println("Successfully assigned \"" + task.getName() + "\" to " + employeeList.get(choice-1).getName() + ". Awaiting response.");
            }
            else System.out.println("Invalid option.");

        } while (choice < 0);

        saveTasks();

    }

    //Staff member accepts or declines task assigned by their manager
    public void answerTask (Staff s) {

        List<Task> taskList = s.getUnrespondedTasks();

        int choice;
        //Begin selection of task to accept
        do {
            System.out.println("\nSelect task to accept/decline:");
            
            for (int i=1; i<=taskList.size(); i++) {
                System.out.println(i + ". " + taskList.get(i-1).getName() + " (Assigned by " + taskList.get(i-1).getCreator().getName() + ", Due " + taskList.get(i-1).getDeadline() + "): AWAITING RESPONSE");
            }

            System.out.println("0: Cancel");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            //cancel or accept task
            if (choice == 0) System.out.println("Returning...");
            else if (choice < taskList.size()+1) {
                Task task = taskList.get(choice-1);
                taskResponse(task);
            }
            else System.out.println("Invalid option.");

        } while (choice < 0);

    }

    //Choose to accept or decline task
    private void taskResponse (Task t) {

        int choice;
        //Begin selection of task to accept
        do {
            System.out.println("1: Accept Task");
            System.out.println("2: Decline Task");
            System.out.println("0: Cancel");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    t.setAccepted(true);
                    //Success message
                    System.out.println("Successfully accepted task.");
                }
                case 2 -> {
                    t.unassign();
                    //Success message
                    System.out.println("Successfully declined task.");
                }
                case 0 -> System.out.println("Returning...");
                default -> System.out.println("Invalid option.");
            }

        } while (choice < 0);

        saveTasks();

    }

    //Staff member marks assigned task as completed
    public void completeTask (Staff s) {

        List<Task> taskList = s.getInProgressTasks();

        int choice;
        //Begin selection of task to accept
        do {
            System.out.println("\nSelect task to mark as completed:");
            
            for (int i=1; i<=taskList.size(); i++) {
                System.out.println(i + ". " + taskList.get(i-1).getName() + " (Assigned by " + taskList.get(i-1).getCreator().getName() + ", Due " + taskList.get(i-1).getDeadline() + "): IN PROGRESS");
            }

            System.out.println("0: Cancel");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            //cancel or complete task
            if (choice == 0) System.out.println("Returning...");
            else if (choice < taskList.size()+1) {

                //Staff member enters comments for their submission
                System.out.println("Enter comments for your submission:");
                Scanner sc2 = new Scanner(System.in);
                String comm = sc2.nextLine();

                //Get current date and time of submission
                LocalDateTime now = LocalDateTime.now();

                //Get task and complete it
                Task task = taskList.get(choice-1);
                task.complete(comm, now);

                //Success message
                System.out.println("Successfully completed task.");
            }
            else System.out.println("Invalid option.");

        } while (choice < 0);

        saveTasks();

    }

    //Save tasks to file
    private void saveTasks() {

        List<String> lines = new ArrayList<>();

        for (Task task : allTasks){

            lines.add("BEGIN TASK");
            lines.add("CREATOR_ID ~ " + task.getCreator().getID());

            //If assigned, write assignee ID. Else, write -1.
            if (task.isAssigned()) lines.add("ASSIGNEE_ID ~ " + task.getAssignee().getID());
            else lines.add("ASSIGNEE_ID ~ -1");

            lines.add("NAME ~ " + task.getName());
            lines.add("DESCRIPTION ~ " + task.getDescription());
            lines.add("DEADLINE ~ " + task.getDeadline());
            lines.add("ACCEPTED ~ " + task.isAccepted());
            lines.add("COMPLETED ~ " + task.isCompleted());

            //If task is completed, save additional related fields
            if (task.isCompleted()) {

                lines.add("COMMENTS ~ " + task.getComments());
                lines.add("SUBMIT_DATETIME ~ " + task.getSubmitDateTime());
            }

            lines.add("END TASK");
            lines.add("");
            
        }
        FileManager.writeLines(filename, lines);

    }

    //Load tasks from file
    private void loadTasks() {

        List<String> lines = util.FileManager.readLines(filename);
        Task currentTask;

        //Initialize variables for each task
        Manager creator = null;
        Staff assignee = null;
        String name = null;
        String desc = null;
        LocalDateTime deadline = null;
        boolean assigned = false;
        boolean completed = false;
        boolean accepted = false;
        String comments = null;
        LocalDateTime submitDateTime = null;

        for (String line : lines) {

            //Get variable that the current line is listing
            String lineStart = line.split(" ~")[0];

            switch (lineStart) {

                case "CREATOR_ID" -> {

                    int creator_id = Integer.parseInt(line.split("~ ")[1]);
                    creator = (Manager) hr.getStaffById(creator_id);
                } 
                case "ASSIGNEE_ID" -> {

                    int assignee_id = Integer.parseInt(line.split("~ ")[1]);
                    if (assignee_id == -1) {
                        assignee = null;
                        assigned = false;
                    } else {
                        assignee = hr.getStaffById(assignee_id);
                        assigned = true;
                    }
                } 
                case "NAME" ->name = line.split("~ ")[1];
                case "DESCRIPTION" -> desc = line.split("~ ")[1];
                case "DEADLINE" -> {
                    //Parse date string
                    String[] dline = line.split("~ ")[1].split("T");
                    String dateText = dline[0];
                    String timeText = dline[1];
                    //Get date time object from text input
                    deadline = dateStringToObject(dateText, timeText);
                }
                case "ACCEPTED" -> accepted = Boolean.parseBoolean(line.split("~ ")[1]);
                case "COMPLETED" -> completed = Boolean.parseBoolean(line.split("~ ")[1]);
                case "COMMENTS" -> comments = line.split("~ ")[1];
                case "SUBMIT_DATETIME" -> {
                    //Parse date string
                    String[] sdt = line.split("~ ")[1].split("T");
                    String dateText = sdt[0];
                    String timeText = sdt[1];
                    //Get date time object from text input
                    submitDateTime = dateStringToObject(dateText, timeText);
                }
                case "END TASK" -> {

                    //Create the task and add it
                    currentTask = new Task(creator, assignee, name, desc, deadline, assigned, completed, accepted, comments, submitDateTime);
                    allTasks.add(currentTask);
                    creator.addTask(currentTask);
                    //If task is assigned but not completed, add to assignee's task list
                    if (assigned && !completed) assignee.addTask(currentTask);
                }

            }

        } 
    }

    //Convert DateTime string into object
    private LocalDateTime dateStringToObject (String dateText, String timeText) {

        String[] dateParts = dateText.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        LocalDate dueDate = LocalDate.of(year, month, day);

        String[] timeParts = timeText.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        LocalTime dueTime = LocalTime.of(hour, minute);
        return LocalDateTime.of(dueDate, dueTime);

    }

}
