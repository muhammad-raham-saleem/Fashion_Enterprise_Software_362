package service;

import model.DesignRepository;
import model.Sketch;
import java.util.Scanner;

public class SketchService {
    private DesignRepository repo;
    private Scanner sc;

    public SketchService(DesignRepository repo, Scanner sc) {
        this.repo = repo;
        this.sc = sc;
    }

    public void createSketch() {
        sc.nextLine();
        System.out.print("Enter sketch name: ");
        String name = sc.nextLine();
        System.out.print("Enter theme: ");
        String theme = sc.nextLine();
        System.out.print("Approve this sketch? (y/n): ");
        String ans = sc.nextLine();
        boolean approved = ans.equalsIgnoreCase("y");
        Sketch s = new Sketch(name, theme, approved);
        repo.addSketch(s);
        System.out.println("Sketch added: " + s);
    }

    public void viewSketches() {
        if (repo.getSketches().isEmpty()) System.out.println("No sketches found.");
        else repo.getSketches().forEach(System.out::println);
    }
}
