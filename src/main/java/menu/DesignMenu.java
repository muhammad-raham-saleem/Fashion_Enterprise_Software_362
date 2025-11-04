package menu;

import model.DesignRepository;
import service.*;
import util.FileManager;
import java.util.Scanner;

public class DesignMenu {
    private final Scanner sc;
    private final SketchService sketchService;
    private final MaterialService materialService;
    private final ManufacturingService manufacturingService;
    private final CostService costService;
    private final DesignRepository repo;

    public DesignMenu(Scanner sc, SketchService sketchService, MaterialService materialService,
                      ManufacturingService manufacturingService, CostService costService,
                      DesignRepository repo) {
        this.sc = sc;
        this.sketchService = sketchService;
        this.materialService = materialService;
        this.manufacturingService = manufacturingService;
        this.costService = costService;
        this.repo = repo;
    }

    public void start() {
        int choice;
        do {
            System.out.println("\n--- DESIGN MENU ---");
            System.out.println("1. Create Sketch");
            System.out.println("2. Select Material");
            System.out.println("3. Verify Fabric Availability");
            System.out.println("4. Confirm Production Costs");
            System.out.println("5. View All Sketches");
            System.out.println("6. View All Materials");
            System.out.println("7. Save Data");
            System.out.println("0. Return to Main Menu");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> sketchService.createSketch();
                case 2 -> materialService.selectMaterial();
                case 3 -> manufacturingService.verifyAvailability();
                case 4 -> costService.confirmCosts();
                case 5 -> sketchService.viewSketches();
                case 6 -> materialService.viewMaterials();
                case 7 -> FileManager.saveData(repo.getSketches(), repo.getMaterials());
                case 0 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }
}

