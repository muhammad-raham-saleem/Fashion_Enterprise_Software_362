package shipping;
import java.util.Scanner;

public class ShipProductToStoreUseCase {
    private final TransferService transferService;

    public ShipProductToStoreUseCase(TransferService service) {
        this.transferService = service;
    }

    public void run(Scanner in) {
        System.out.print("Enter Transfer ID: ");
        String id = in.nextLine();
        System.out.print("Carrier: ");
        String carrier = in.nextLine();
        System.out.print("Tracking Number: ");
        String tracking = in.nextLine();
        System.out.print("Ship Date (YYYY-MM-DD): ");
        String date = in.nextLine();

        transferService.shipTransfer(id, carrier, tracking, date);
    }
}
