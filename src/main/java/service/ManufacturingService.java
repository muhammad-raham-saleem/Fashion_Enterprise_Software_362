package service;

import model.DesignRepository;
import model.Material;

public class ManufacturingService {
    private DesignRepository repo;

    public ManufacturingService(DesignRepository repo) {
        this.repo = repo;
    }

    public void verifyAvailability() {
        if (repo.getMaterials().isEmpty()) {
            System.out.println("No materials to verify.");
            return;
        }
        for (Material m : repo.getMaterials()) {
            if (m.isAvailable()) System.out.println(m.getName() + " is available.");
            else System.out.println(m.getName() + " is OUT OF STOCK!");
        }
    }
}
