package menu;

import model.*;
import service.MaterialPrototypeService;
import java.util.Scanner;

public class MaterialPrototypeMenu {
    private final Scanner sc;
    private final MaterialPrototypeService prototypeService;
    private final RDScientist scientist;
    private final ManufacturingTechnician technician;
    private final DesignTeamMember designer;

    public MaterialPrototypeMenu(Scanner sc, MaterialPrototypeService prototypeService) {
        this.sc = sc;
        this.prototypeService = prototypeService;
        // Initialize default team members for testing
        this.scientist = new RDScientist("Dr. Emily Chen", "R&D", "Senior Scientist", 95000);
        this.technician = new ManufacturingTechnician("Mike Rodriguez", "Manufacturing", "Lead Technician", 65000);
        this.designer = new DesignTeamMember("Alice Williams", "Design", "Senior Designer", 75000);
    }

    public void start() {
        int choice;
        do {
            System.out.println("\n=== MATERIAL PROTOTYPE DEVELOPMENT MENU ===");
            System.out.println("1. Create Material Specification");
            System.out.println("2. Create New Prototype (R&D Scientist)");
            System.out.println("3. View All Prototypes");
            System.out.println("4. View Prototype Details");
            System.out.println("5. Manufacturing Test");
            System.out.println("6. Design Evaluation");
            System.out.println("7. Refine Prototype (R&D Scientist)");
            System.out.println("8. Approve/Reject Prototype");
            System.out.println("0. Return to Main Menu");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> prototypeService.createSpecification();
                case 2 -> {
                    System.out.println("\n=== R&D SCIENTIST MODE ===");
                    System.out.println("Logged in as: " + scientist.getName());
                    prototypeService.createPrototype(scientist);
                }
                case 3 -> prototypeService.viewAllPrototypes();
                case 4 -> prototypeService.viewPrototypeDetails();
                case 5 -> {
                    System.out.println("\n=== MANUFACTURING TECHNICIAN MODE ===");
                    System.out.println("Logged in as: " + technician.getName());
                    prototypeService.manufacturingTest(technician);
                }
                case 6 -> {
                    System.out.println("\n=== DESIGN TEAM MEMBER MODE ===");
                    System.out.println("Logged in as: " + designer.getName());
                    prototypeService.designEvaluation(designer);
                }
                case 7 -> {
                    System.out.println("\n=== R&D SCIENTIST MODE ===");
                    System.out.println("Logged in as: " + scientist.getName());
                    prototypeService.refinePrototype(scientist);
                }
                case 8 -> prototypeService.approvePrototype();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option. Try again.");
            }
        } while (choice != 0);
    }
}
