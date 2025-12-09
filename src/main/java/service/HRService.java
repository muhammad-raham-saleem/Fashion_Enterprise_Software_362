package service;

import model.*;
import java.util.*;
import util.FileManager;

public class HRService {

    private HR hr;
    private Scanner sc;
    private String filename;
    
    private ReviewService reviewService;
    
    public HRService (HR hr, Scanner sc, String f) {
        this.hr = hr;
        this.sc = sc;
        this.filename = f;
        loadStaff();
    }

    public void addReviewService (ReviewService rs) {
        reviewService = rs;
    }

    public void viewStaffList() {

        if (this.hr.getStaff().isEmpty()) System.out.println("No staff hired.");
        else {
            for (int i=hr.getStaff().size(); i>0; i--) {
                Staff s = hr.getStaffById(i);
                System.out.println(s.getID() + ": " + s.getName() + " (" + s.getDepartment() + ")");
                System.out.println("Role: " + s.getRole() + ", Salary: $" + s.getSalary() + "\n");
            }
        }
        
    }

    public Staff createStaff (String name, String dep, String role, int salary, boolean isManager) {

        Staff newStaff;
        if (isManager) {
            newStaff = new Manager(name, dep, role, salary);
        } else {
            newStaff = new Staff(name, dep, role, salary);
        }
        
        hr.addStaff(newStaff);
        return newStaff;

    }

    public void hireStaff () {

        sc.nextLine();
        System.out.println("Enter employee name:");
        String name = sc.nextLine();
        System.out.println("Enter name of department " + name + " will be working in:");
        String department = sc.nextLine();
        System.out.println("Enter " + name + "'s role:");
        String role = sc.nextLine();
        System.out.println("Enter " + name + "'s salary:");
        int salary = sc.nextInt();

        createStaff(name, department, role, salary, false);
        saveStaff();

    }

    private void saveStaff() {
        List<String> lines = new ArrayList<>();
        lines.add("name, id, department, role, salary, isManager, manager");

        for (Staff s : this.hr.getStaff()){
            String line = s.getName() + ", " + s.getID() + ", " + s.getDepartment() + ", " + s.getRole() + ", " + s.getSalary();
            if (s instanceof Manager) line += ", true";
            else line += ", false, " + s.getManager().getID();
            lines.add(line);
        }
        FileManager.writeLines(filename, lines);
    }

    private void loadStaff() {
        List<String> lines = util.FileManager.readLines(filename);

        for (String line : lines){
            if (line.startsWith("name, ")) continue;

            String [] parts = line.split(", ");
            String name = parts[0];
            String dep = parts[2];
            String role = parts[3];
            int salary = Integer.parseInt(parts[4]);
            boolean isManager = Boolean.parseBoolean(parts[5]);

            //Create staff from file
            Staff newStaff = createStaff(name, dep, role, salary, isManager); 
            //If not manager, set this staff member's manager
            if (!isManager) {
                int manager_id = Integer.parseInt(parts[6]);
                Manager man = (Manager) hr.getStaffById(manager_id);
                newStaff.setManager(man);
                man.addEmployee(newStaff);
            }
        } 
    }

    public void sendReview (EmployeeReview r) {
        hr.addReview(r);
    }

    public void approveReview () {

        List<EmployeeReview> reviews = hr.getReviews();
        
        int choice = 0;
        do {

            System.out.println("\n--- REVIEWS ---");
            if (reviews.isEmpty()) System.out.println("There are no reviews.");

            for (int i=1; i<=reviews.size(); i++) {

                EmployeeReview current = reviews.get(i-1);
                System.out.print(i + ". Review for " + current.getReviewee().getName());
                System.out.print(", " + current.getDateTime());
                System.out.println(" (AWAITING APPROVAL)");

            }

            //Next steps
            System.out.println("\nEnter Review Number to approve/disapprove.");
            System.out.println("Or Type 0 to Go Back.");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            if (choice == 0) System.out.println("Returning...");
            else if (choice < reviews.size()+1) {
                EmployeeReview review = reviews.get(choice-1);
                answerReview(review);
            }
            else System.out.println("Invalid option.");

        } while (choice > 0 && choice < reviews.size()+1);

    }

    private void answerReview (EmployeeReview review) {

        int choice;
        //Begin selection of task to accept
        do {
            System.out.println("1: Approve Review");
            System.out.println("2: Disapprove Review");
            System.out.println("0: Cancel");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    review.approve();
                    hr.getReviews().remove(review);
                    review.getReviewee().addReview(review);
                    saveStaff();
                    //Success message
                    System.out.println("Successfully approved review.");
                }
                case 2 -> {
                    hr.getReviews().remove(review);
                    reviewService.deleteReview(review);
                    //Success message
                    System.out.println("Successfully disapproved review.");
                }
                case 0 -> System.out.println("Returning...");
                default -> System.out.println("Invalid option.");
            }

        } while (choice < 0);

        reviewService.saveReviews();

    }

}