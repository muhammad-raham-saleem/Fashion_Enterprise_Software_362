package service;

import model.DesignRepository;
import java.util.Scanner;

public class CostService {
    private DesignRepository repo;
    private Scanner sc;

    public CostService(DesignRepository repo, Scanner sc) {
        this.repo = repo;
        this.sc = sc;
    }

    public void confirmCosts() {
        int totalSketches = repo.getSketches().size();
        int totalMaterials = repo.getMaterials().size();
        int cost = (totalSketches * 200) + (totalMaterials * 150);

        System.out.println("Estimated total cost: $" + cost);
        sc.nextLine();
        System.out.print("Approve production? (y/n): ");
        String ans = sc.nextLine();
        if (ans.equalsIgnoreCase("y")) System.out.println("Production approved.");
        else System.out.println("Production postponed for review.");
    }
}
