import model.DesignRepository;
import service.*;
import util.FileManager;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DesignRepository repo = new DesignRepository();

        SketchService sketchService = new SketchService(repo, sc);
        MaterialService materialService = new MaterialService(repo, sc);
        ManufacturingService manufacturingService = new ManufacturingService(repo);
        CostService costService = new CostService(repo, sc);

        FileManager.loadData(repo.getSketches(), repo.getMaterials());

        int choice;
        do {
            System.out.println("\n=== DESIGN NEW COLLECTION MENU ===");
            System.out.println("1. Create Sketch");
            System.out.println("2. Select Material");
            System.out.println("3. Verify Fabric Availability");
            System.out.println("4. Confirm Production Costs");
            System.out.println("5. View All Sketches");
            System.out.println("6. View All Materials");
            System.out.println("7. Save Data");
            System.out.println("0. Exit");
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
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);

        sc.close();
    }
}
