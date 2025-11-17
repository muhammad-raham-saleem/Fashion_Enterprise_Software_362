package menu;

import java.util.Scanner;
import model.*;
import service.TaskService;

public class StaffMenu {

    private final Scanner sc;
    private final HR hr;
    private final TaskService taskService;

    private Staff user; //Staff member using the menu

    public StaffMenu (Scanner sc, HR hr, TaskService ts) {
        this.sc = sc;
        this.hr = hr;
        taskService = ts;
    }

    public void start() {

        //Enter employee ID to identify who is doing the action
        System.out.print("Enter your employee ID: ");
        int id = sc.nextInt();
        user = hr.getStaffById(id);

        //Welcome message
        System.out.println("Welcome, " + user.getName() + "!");

        //Decide menu based on type of staff
        if (user instanceof Manager) managerMode();
        else staffMode();

    }

    //Menu for regular staff
    private void staffMode () {

        int choice;
        do {
            System.out.println("\nWhat do you want to do?");
            System.out.println("1. View Your Tasks");
            System.out.println("2. Accept or Decline Pending Task");
            System.out.println("3. Complete Task");
            System.out.println("0. Cancel");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> taskService.viewTasks(user);
                case 2 -> taskService.answerTask(user);
                case 3 -> taskService.completeTask(user);
                case 0 -> System.out.println("Returning...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);

    }

    //Menu for managers
    private void managerMode () {

        Manager user_m = (Manager) user;

        int choice;
        do {
            System.out.println("\nWhat do you want to do?");
            System.out.println("1. View Your Created Tasks");
            System.out.println("2. Create New Task");
            System.out.println("3. Assign Task to Employee");
            System.out.println("4. Edit Task");
            System.out.println("5. Unassign or Cancel Task");
            System.out.println("0. Cancel");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> taskService.viewTasks(user_m);
                case 2 -> taskService.createTask(user_m);
                case 3 -> taskService.beginAssignment(user_m);
                //case 4 -> taskService.editTask(user_m);
                //case 5 -> taskService.cancelTask(user_m);
                case 0 -> System.out.println("Returning...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);

    }

}
