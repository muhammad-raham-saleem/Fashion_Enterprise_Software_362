package menu;

import model.DesignRepository;
import service.*;
import util.FileManager;
import java.util.Scanner;

public class DesignMenu implements Menu {
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

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
            new MenuOption(1, "Create Sketch", sketchService::createSketch),
            new MenuOption(2, "Select Material", materialService::selectMaterial),
            new MenuOption(3, "Verify Fabric Availability", manufacturingService::verifyAvailability),
            new MenuOption(4, "Confirm Production Costs", costService::confirmCosts),
            new MenuOption(5, "View All Sketches", sketchService::viewSketches),
            new MenuOption(6, "View All Materials", materialService::viewMaterials),
            new MenuOption(7, "Save Data", () -> FileManager.saveData(repo.getSketches(), repo.getMaterials())),
            new MenuOption(0, "Return to Main Menu", () -> System.out.println("Returning to Main Menu..."))
        };
    }

    @Override
    public void start() {
        int choice;
        do {
            System.out.println("\n--- DESIGN MENU ---");
            displayOptions();
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            executeOption(choice);
        } while (choice != 0);
    }
}

