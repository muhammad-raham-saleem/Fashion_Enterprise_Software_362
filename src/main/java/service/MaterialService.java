package service;

import model.DesignRepository;
import model.Material;
import java.util.Scanner;

public class MaterialService {
    private DesignRepository repo;
    private Scanner sc;

    public MaterialService(DesignRepository repo, Scanner sc) {
        this.repo = repo;
        this.sc = sc;
    }

    public void selectMaterial() {
        sc.nextLine();
        System.out.print("Enter material name: ");
        String name = sc.nextLine();
        System.out.print("Is it available? (y/n): ");
        String available = sc.nextLine();
        boolean isAvailable = available.equalsIgnoreCase("y");
        Material m = new Material(name, isAvailable);
        repo.addMaterial(m);
        System.out.println("Material added: " + m);
    }

    public void viewMaterials() {
        if (repo.getMaterials().isEmpty()) System.out.println("No materials found.");
        else repo.getMaterials().forEach(System.out::println);
    }
}
