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

    public Task createTask (Manager creator) {

        sc.nextLine();
        System.out.println("Enter task name:");
        String name = sc.nextLine();
        System.out.println("Enter description of task:");
        String desc = sc.nextLine();

        System.out.println("Enter deadline date (YYYY-MM-DD):");
        String dateText = sc.nextLine();

        String[] dateParts = dateText.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        LocalDate dueDate = LocalDate.of(year, month, day);

        System.out.println("Enter deadline time (HH:MM) (24 hour time):");
        String timeText = sc.nextLine();

        String[] timeParts = timeText.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        LocalTime dueTime = LocalTime.of(hour, minute);
        LocalDateTime deadline = LocalDateTime.of(dueDate, dueTime);

        Task newTask = new Task(creator, name, desc, deadline);

        allTasks.add(newTask);
        saveTasks();

        int choice;
        do {
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
        } while (choice != 2);

        return newTask;

    }

    public void assignTask (Task task, Manager creator) {

        int choice;
        do {
            System.out.println("\nWho would you like to assign this task to?");

            List<Staff> employeeList = creator.getAvailableEmployees();
            for (int i=1; i<=employeeList.size(); i++) {
                System.out.println(i + ": " + employeeList.get(i).getName());
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
            else if (choice < employeeList.size()+1) task.assign(employeeList.get(choice-1));
            else System.out.println("Invalid option.");

        } while (choice != 0);

        saveTasks();

    }

    private void saveTasks() {

        List<String> lines = new ArrayList<>();

        for (Task task : allTasks){

            lines.add("BEGIN TASK");
            lines.add("CREATOR_ID ~ " + task.getCreator().getID());

            //If assigned, write assignee ID. Else, write -1.
            if (task.isAssigned()) lines.add("ASIGNEE_ID ~ " + task.getAssignee().getID());
            else lines.add("ASIGNEE_ID ~ -1");

            lines.add("NAME ~ " + task.getName());
            lines.add("DESCRIPTION ~ " + task.getDescription());
            lines.add("DEADLINE ~ " + task.getDeadline());
            lines.add("COMPLETED: ~ " + task.isCompleted());
            lines.add("END TASK");
            lines.add("");
            
        }
        FileManager.writeLines(filename, lines);

    }

    private void loadTasks() {

        List<String> lines = util.FileManager.readLines(filename);
        Task currentTask = null;

        //Initialize variables for each task
        Manager creator = null;
        Staff assignee = null;
        String name = null;
        String desc = null;
        LocalDateTime deadline = null;
        boolean assigned = false;
        boolean completed = false;

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
                case "DEADLINE" -> desc = line.split("~ ")[1];
                case "COMPLETED" -> completed = Boolean.parseBoolean(line.split("~ ")[1]);
                case "END TASK" -> {

                    //Create the task and add it
                    currentTask = new Task(creator, assignee, name, desc, deadline, assigned, completed);
                    allTasks.add(currentTask);
                }

            }

        } 
    }

}
