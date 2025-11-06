package shipping;

import java.util.*;

public class TransferService {
    private final List<Transfer> transfers = new ArrayList<>();

    public void addTransfer(Transfer t) { transfers.add(t); }

    public void shipTransfer(String id, String carrier, String tracking, String date) {
        for (Transfer t : transfers) {
            if (t.getId().equals(id)) {
                if (!t.getStatus().equals("Draft")) {
                    System.out.println("Error: Already shipped or invalid state.");
                    return;
                }
                t.ship(carrier, tracking, date);
                System.out.println("Transfer " + id + " shipped successfully!");
                return;
            }
        }
        System.out.println("Transfer not found.");
    }

    public void listTransfers() {
        for (Transfer t : transfers) System.out.println(t);
    }
}

