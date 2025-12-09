package menu;

import model.*;
import service.*;
import util.FileManager;

import java.util.Scanner;

public class MainMenu implements Menu {
    private final Scanner sc = new Scanner(System.in);

    // Shared resources (repositories and services)
    private final DesignRepository designRepo = new DesignRepository();
    private final SketchService sketchService = new SketchService(designRepo, sc);
    private final MaterialService materialService = new MaterialService(designRepo, sc);
    private final ManufacturingService manufacturingService = new ManufacturingService(designRepo);
    private final CostService costService = new CostService(designRepo, sc);
    private final Inventory inventory = new Inventory("data/inventory.txt");
    private final FinanceService financeService = new FinanceService("data/finance.txt");
    private final SaleService saleService = new SaleService(sc, inventory, financeService);
    private final ReturnService returnService = new ReturnService(sc, financeService);
    private final HR hr = new HR();
    private final HRService hrService = new HRService(hr, sc, "data/staff.txt");
    private final TaskService taskService = new TaskService(hr, sc, "data/tasks.txt");
    private final ShippingDepartmentMenu shipMenu = new ShippingDepartmentMenu(sc);
    private final CampaignService campaignService = new CampaignService(sc);
    private final model.EventRepository eventRepository = new model.EventRepository("data/events.csv");
    private final model.CustomerRepository customerRepository = new model.CustomerRepository("data/customers.csv");
    private final service.EventCoordinatorService eventCoordinatorService = new service.EventCoordinatorService(
            eventRepository, customerRepository, hr, financeService, new util.BrowserEmailHandler());
    private final VendorContractRepository vendorContractRepository = new VendorContractRepository("data/vendors.txt", "data/contracts.txt");
    private final VendorContractService vendorContractService = new VendorContractService(sc, vendorContractRepository);
    private final MaterialPrototypeRepository materialPrototypeRepository = new MaterialPrototypeRepository("data/specifications.txt", "data/prototypes.txt", "data/feedback.txt");
    private final MaterialPrototypeService materialPrototypeService = new MaterialPrototypeService(sc,
            materialPrototypeRepository);
    private final ProductRepository productRepository = new ProductRepository("data/products.txt");
    private final PurchaseOrderRepository purchaseOrderRepository = new PurchaseOrderRepository("data/purchase_orders.csv");
    private final PurchaseOrderService purchaseOrderService = new PurchaseOrderService(purchaseOrderRepository,
            productRepository, vendorContractRepository, inventory);
        private final ReportService reportService = new ReportService(
            new util.LogManager("data/salesLogs.txt"));
    private final model.Schedule schedule = new model.Schedule();
    private final model.Events events = new model.Events();
    private final FashionShowService fashionShowService = new FashionShowService(schedule, events, financeService);

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
                new MenuOption(1, "Design Department",
                        () -> new DesignMenu(sc, sketchService, materialService, manufacturingService, costService,
                                designRepo).start()),
                new MenuOption(2, "Warehouse Department", () -> new WarehouseMenu(sc).start()),
                new MenuOption(3, "Sales Department", () -> new SalesMenu(sc, saleService, returnService, reportService).start()),
                new MenuOption(4, "HR Department", () -> new HRMenu(sc, hr, hrService).start()),
                new MenuOption(5, "Shipping Department", shipMenu::start),
                new MenuOption(6, "Marketing Menu",
                        () -> new MarketingMenu(sc, campaignService, eventCoordinatorService).start()),
                new MenuOption(7, "Staff Menu", () -> new StaffMenu(sc, hr, taskService).start()),
                new MenuOption(8, "Finance Department", () -> new FinanceMenu(sc, financeService, eventCoordinatorService).start()),
                new MenuOption(9, "Vendor Contract", () -> new VendorContractMenu(sc, vendorContractService).start()),
                new MenuOption(10, "Material Prototype Menu",
                        () -> new MaterialPrototypeMenu(sc, materialPrototypeService).start()),
                new MenuOption(11, "Purchase Order Management",
                        () -> new PurchaseOrderMenu(sc, purchaseOrderService).start()),
                new MenuOption(12, "Fashion Show Management",
                        () -> new FashionShowMenu(sc, fashionShowService).start()),
                new MenuOption(0, "Exit", () -> System.out.println("Goodbye!"))
        };
    }
    @Override
    public void start() {
        // Load data before menus
        FileManager.loadData(designRepo.getSketches(), designRepo.getMaterials());

        int choice;
        do {
            System.out.println("\n=== MAIN MENU ===");
            displayOptions();
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            executeOption(choice);

        } while (choice != 0);

        sc.close();
    }
}
