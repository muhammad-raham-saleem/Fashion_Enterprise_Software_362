package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import model.*;
import model.EmployeeReview.Rating;
import util.FileManager;

public class ReviewService {

    private HR hr; //Needs HR to access staff database
    private HRService hrService;
    private TaskService taskService;
    private Scanner sc;
    private String filename;

    private List<EmployeeReview> reviews = new ArrayList<>();

    private String[] categories = {
            "quality",
            "timeliness"
        };

    public ReviewService (Scanner sc, String f, HR hr, HRService hrs, TaskService ts) {

        this.hr = hr;
        hrService = hrs;
        taskService = ts;
        this.sc = sc;
        this.filename = f;
        loadReviews();

    }

    //View all reviews
    public void viewReviews(Staff s) {

        int choice = 0;
        do {

            System.out.println("\n--- REVIEWS ---");
            if (reviews.isEmpty()) System.out.println("There are no reviews.");

            for (int i=1; i<=reviews.size(); i++) {

                EmployeeReview current = reviews.get(i-1);
                System.out.print(i + ". Review for " + current.getReviewee().getName());
                System.out.print(", " + current.getDateTime());

                //If manager, show approval status
                if (s instanceof Manager) {
                    if (current.isApproved()) System.out.println(" (APPROVED)");
                    else System.out.println(" (AWAITING APPROVAL)");
                }
                else System.out.println("");

            }

            //Next steps
            System.out.println("\nEnter Review Number to View Details.");
            System.out.println("Or Type 0 to Go Back.");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            if (choice == 0) System.out.println("Returning...");
            else if (choice < reviews.size()+1) {
                EmployeeReview review = reviews.get(choice-1);
                viewReviewDetails(review);
            }
            else System.out.println("Invalid option.");

        } while (choice != 0);

    }

    //Print out all details of a review
    private void viewReviewDetails (EmployeeReview review) {

        System.out.println("REVIEWER: " + review.getReviewer().getName() + " (" + review.getReviewer().getDepartment() + ")");
        System.out.print("STATUS: ");
        if (review.isApproved()) System.out.println("APPROVED");
        else System.out.println("AWAITING APPROVAL");

        System.out.println("TASKS EVALUATED:");
        for (Task task : review.getTasks()) {

            System.out.println("\nNAME: " + task.getName());
            System.out.println("ASSIGNED BY: " + task.getCreator().getName() + " (" + task.getCreator().getDepartment() + ")");

            System.out.print("COMPLETED ");
            if (task.onTime()) System.out.println("ON TIME");
            else System.out.println("LATE");
            System.out.println("COMPLETED " + task.getSubmitDateTime().toLocalDate() + " @ " + task.getSubmitDateTime().toLocalTime());
            
            for (Rating rating : review.getRatings(task)) {

                // TODO: List rating stuff
                System.out.println(rating);

            }

        }

        System.out.print("\nPress Enter to continue.");
        Scanner sc2 = new Scanner(System.in);
        sc2.nextLine();

    }

    //Manager conducts the review
    public EmployeeReview conductReview (Manager reviewer) {

        List<Staff> employees = reviewer.getAllEmployees();
        EmployeeReview review = null;
        Staff reviewee = null;
        
        //Selection of employee to be reviewed
        int choice = 0;
        do {
            System.out.println("\nSelect employee to review:");
            
            for (int i=1; i<=employees.size(); i++) {
                System.out.println(i + ": " + employees.get(i-1).getName() + " (" + employees.get(i-1).getRole() + ")");
            }

            System.out.println("0: Cancel");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            if (choice == 0) return null;
            else if (choice < employees.size()+1) {
                reviewee = employees.get(choice-1);
                review = new EmployeeReview(reviewer, reviewee);
            }
            else System.out.println("Invalid option.");

        } while (choice < 0);

        List<Task> taskList = reviewee.getCompletedTasks();
        choice = 0;

        //Selection of tasks to be reviewed
        do {
            //Next steps
            System.out.println("\nSelect Task to add to review");

            if (taskList.isEmpty()) System.out.println("There are no tasks.");

            for (int i=1; i<=taskList.size(); i++) {

                Task currTask = taskList.get(i-1);

                System.out.print(i + ". " + currTask.getName());
                System.out.print(" (Assigned to " + currTask.getAssignee().getName() + "): ");

                System.out.print("COMPLETED ");
                //Print on time status
                if (currTask.onTime()) System.out.println("ON TIME");
                else System.out.println("LATE");

            }

            //Next steps
            System.out.println("\nEnter Task Number to Add.");
            System.out.println("Or Type 0 to Finish.");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            if (choice == 0) {}
            else if (review.getTasks().contains(taskList.get(choice-1))) {
                System.out.println("Task already included.");
            }
            else if (choice < taskList.size()+1) {
                Task task = taskList.get(choice-1);
                reviewTask(review, task);
            }
            else System.out.println("Invalid option.");
            
        } while (choice != 0);

        System.err.println("Enter overall comments:");
        Scanner sc2 = new Scanner(System.in);
        String comms = sc2.nextLine();
        review.addComments(comms);

        reviews.add(review);
        reviewer.addReview(review);
        hrService.sendReview(review);
        saveReviews();

        //Success message
        System.out.println("Review sent to HR for approval.");

        return review;

    }

    //Individual task review
    private void reviewTask (EmployeeReview review, Task task) {

        review.addTask(task);

        for (String cat: categories) {

            double rating = -1;

            while (true) {

                System.out.print("Rate task completion's " + cat + " out of 10: ");
                rating = sc.nextDouble();

                if (rating >= 0 && rating <= 10) break;
                else System.out.println("Invalid number.");

            }

            System.err.println("Enter comments:");
            Scanner sc2 = new Scanner(System.in);
            String comments = sc2.nextLine();

            review.rateTask(task, cat, rating, comments);

        }

    }

    //Save reviews to file
    private void saveReviews() {

        List<String> lines = new ArrayList<>();

        for (EmployeeReview review : reviews){

            lines.add("BEGIN REVIEW");
            lines.add("REVIEWER_ID ~ " + review.getReviewer().getID());
            lines.add("REVIEWEE_ID ~ " + review.getReviewee().getID());

            lines.add("COMMENTS ~ " + review.getComments());
            lines.add("DATETIME ~ " + review.getDateTime());
            lines.add("APPROVED ~ " + review.isApproved());

            lines.add("BEGIN TASKS");

            for (Task task : review.getTasks()) {

                lines.add("TASK_ID ~ " + task.getID());

                for (Rating rating : review.getRatings(task)) {

                    lines.add("CATEGORY ~ " + rating.getCategory());
                    lines.add("SCORE ~ " + rating.getScore());
                    lines.add("COMMS ~ " + rating.getComments());

                }

                lines.add("TASK END");
            }

            lines.add("END TASKS");

            lines.add("END REVIEW");
            lines.add("");
            
        }
        FileManager.writeLines(filename, lines);

    }

    //Load tasks from file
    private void loadReviews() {

        List<String> lines = util.FileManager.readLines(filename);
        EmployeeReview current = null;

        //Initialize variables for each task
        Manager reviewer = null;
        Staff reviewee = null;
        LocalDateTime dateTime = null;
        String comments = null;
        boolean approved = false;

        //Initialize rating varaibles
        int task_id = -1;
        String category = null;
        double score = -1;
        String rateComms = null;

        for (int i=0; i<lines.size(); i++) {

            String line = lines.get(i);

            //Get variable that the current line is listing
            String lineStart = line.split(" ~")[0];

            switch (lineStart) {

                case "REVIEWER_ID" -> {

                    int reviewer_id = Integer.parseInt(line.split("~ ")[1]);
                    reviewer = (Manager) hr.getStaffById(reviewer_id);
                } 
                case "REVIEWEE_ID" -> {

                    int reviewee_id = Integer.parseInt(line.split("~ ")[1]);
                    reviewee = hr.getStaffById(reviewee_id);

                }
                case "COMMENTS" -> comments = line.split("~ ")[1];
                case "DATETIME" -> {
                    //Parse date string
                    String[] dline = line.split("~ ")[1].split("T");
                    String dateText = dline[0];
                    String timeText = dline[1];
                    //Get date time object from text input
                    dateTime = dateStringToObject(dateText, timeText);
                }
                case "APPROVED" -> approved = Boolean.parseBoolean(line.split("~ ")[1]);
                case "BEGIN TASKS" -> 
                    current = new EmployeeReview(reviewer, reviewee, dateTime, approved);
                case "TASK_ID" -> task_id = Integer.parseInt(line.split("~ ")[1]);
                case "CATEGORY" -> category = line.split("~ ")[1];
                case "SCORE" -> score = Double.parseDouble(line.split("~ ")[1]);
                case "COMMS" -> rateComms = line.split("~ ")[1];
                case "TASK END" -> {

                    Task newTask = taskService.getTaskById(task_id);
                    current.addTask(newTask);
                    current.rateTask(newTask, category, score, rateComms);

                }
                case "END REVIEW" -> {

                    reviews.add(current);
                    reviewer.addReview(current);
                    //If task is assigned, add to assignee's task list
                    if (approved) reviewee.addReview(current);
                    else hr.addReview(current);
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