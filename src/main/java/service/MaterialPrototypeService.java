package service;

import model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MaterialPrototypeService {
    private final MaterialPrototypeRepository repository;
    private final Scanner sc;

    public MaterialPrototypeService(Scanner sc, MaterialPrototypeRepository repository) {
        this.repository = repository;
        this.sc = sc;
    }

    public void createSpecification() {
        sc.nextLine(); // Consume newline
        System.out.print("Enter texture: ");
        String texture = sc.nextLine();
        System.out.print("Enter weight (grams): ");
        double weight = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter sustainability requirement: ");
        String sustainability = sc.nextLine();
        System.out.print("Enter color: ");
        String color = sc.nextLine();
        System.out.print("Enter finish: ");
        String finish = sc.nextLine();

        MaterialSpecification spec = new MaterialSpecification(texture, weight, sustainability, color, finish);
        repository.addSpecification(spec);
        System.out.println("Specification created successfully! Spec ID: " + spec.getSpecID());
    }

    public void createPrototype(RDScientist scientist) {
        List<MaterialSpecification> specifications = repository.getAllSpecifications();
        if (specifications.isEmpty()) {
            System.out.println("No specifications available. Please create a specification first.");
            return;
        }

        System.out.println("\n=== Available Specifications ===");
        for (MaterialSpecification spec : specifications) {
            System.out.println("Spec ID: " + spec.getSpecID() + " - " + spec.toString());
        }

        System.out.print("\nEnter specification ID: ");
        int specId = sc.nextInt();
        sc.nextLine();

        MaterialSpecification spec = repository.findSpecificationById(specId);
        if (spec == null) {
            System.out.println("Specification not found!");
            return;
        }

        System.out.print("Enter prototype name: ");
        String name = sc.nextLine();

        MaterialPrototype prototype = new MaterialPrototype(name, spec);

        System.out.print("Enter composition: ");
        String composition = sc.nextLine();

        scientist.createPrototype(prototype, composition);
        repository.addPrototype(prototype);

        System.out.println("Prototype created successfully! Prototype ID: " + prototype.getPrototypeID());
        System.out.println("Status updated to: " + prototype.getStatus());
    }

    public void viewAllPrototypes() {
        List<MaterialPrototype> prototypes = repository.getAllPrototypes();
        if (prototypes.isEmpty()) {
            System.out.println("No prototypes available.");
            return;
        }

        System.out.println("\n=== ALL PROTOTYPES ===");
        for (MaterialPrototype proto : prototypes) {
            System.out.println(proto);
        }
    }

    public void viewPrototypeDetails() {
        System.out.print("Enter prototype ID: ");
        int protoId = sc.nextInt();

        MaterialPrototype prototype = repository.findPrototypeById(protoId);
        if (prototype == null) {
            System.out.println("Prototype not found!");
            return;
        }

        prototype.displayDetails();

        if (!prototype.getFeedbackList().isEmpty()) {
            System.out.println("\n=== FEEDBACK ===");
            for (MaterialFeedback feedback : prototype.getFeedbackList()) {
                System.out.println(feedback);
            }
        }
    }

    public void manufacturingTest(ManufacturingTechnician technician) {
        List<MaterialPrototype> prototypes = repository.getAllPrototypes();
        List<MaterialPrototype> testingPrototypes = new ArrayList<>();
        for (MaterialPrototype p : prototypes) {
            if (p.getStatus().equals("Testing")) {
                testingPrototypes.add(p);
            }
        }

        if (testingPrototypes.isEmpty()) {
            System.out.println("No prototypes in testing phase.");
            return;
        }

        System.out.println("\n=== PROTOTYPES IN TESTING ===");
        for (MaterialPrototype proto : testingPrototypes) {
            System.out.println(proto);
        }

        System.out.print("\nEnter prototype ID to test: ");
        int protoId = sc.nextInt();
        sc.nextLine();

        MaterialPrototype prototype = repository.findPrototypeById(protoId);
        if (prototype == null) {
            System.out.println("Prototype not found!");
            return;
        }

        prototype.displayDetails();

        System.out.println("\n=== MANUFACTURABILITY TEST ===");
        System.out.print("Enter test comments: ");
        String comments = sc.nextLine();
        System.out.print("Enter rating (1-5): ");
        int rating = sc.nextInt();
        sc.nextLine();

        MaterialFeedback feedback = technician.testManufacturability(prototype, comments, rating);
        prototype.addFeedback(feedback);
        repository.addFeedback(feedback);
        repository.updatePrototype(prototype);

        System.out.println("Manufacturing feedback added successfully!");
        System.out.println("Rating: " + rating + "/5");

        if (rating < 3) {
            System.out.println("Low rating detected. Prototype may need revision.");
        }
    }

    public void designEvaluation(DesignTeamMember designer) {
        List<MaterialPrototype> prototypes = repository.getAllPrototypes();
        List<MaterialPrototype> testingPrototypes = new ArrayList<>();
        for (MaterialPrototype p : prototypes) {
            if (p.getStatus().equals("Testing")) {
                testingPrototypes.add(p);
            }
        }

        if (testingPrototypes.isEmpty()) {
            System.out.println("No prototypes in testing phase.");
            return;
        }

        System.out.println("\n=== PROTOTYPES IN TESTING ===");
        for (MaterialPrototype proto : testingPrototypes) {
            System.out.println(proto);
        }

        System.out.print("\nEnter prototype ID to evaluate: ");
        int protoId = sc.nextInt();
        sc.nextLine();

        MaterialPrototype prototype = repository.findPrototypeById(protoId);
        if (prototype == null) {
            System.out.println("Prototype not found!");
            return;
        }

        prototype.displayDetails();

        System.out.println("\n=== AESTHETIC EVALUATION ===");
        System.out.print("Enter aesthetic comments: ");
        String comments = sc.nextLine();
        System.out.print("Enter rating (1-5): ");
        int rating = sc.nextInt();
        sc.nextLine();

        MaterialFeedback feedback = designer.evaluateAesthetics(prototype, comments, rating);
        prototype.addFeedback(feedback);
        repository.addFeedback(feedback);
        repository.updatePrototype(prototype);

        System.out.println("Design feedback added successfully!");
        System.out.println("Rating: " + rating + "/5");

        if (rating < 3) {
            System.out.println("Low rating detected. Color/finish may need adjustment.");
        }
    }

    public void refinePrototype(RDScientist scientist) {
        viewAllPrototypes();

        List<MaterialPrototype> prototypes = repository.getAllPrototypes();
        if (prototypes.isEmpty()) {
            return;
        }

        System.out.print("\nEnter prototype ID to refine: ");
        int protoId = sc.nextInt();
        sc.nextLine();

        MaterialPrototype prototype = repository.findPrototypeById(protoId);
        if (prototype == null) {
            System.out.println("Prototype not found!");
            return;
        }

        prototype.displayDetails();

        if (!prototype.getFeedbackList().isEmpty()) {
            System.out.println("\n=== FEEDBACK SUMMARY ===");
            for (MaterialFeedback feedback : prototype.getFeedbackList()) {
                System.out.println(feedback);
            }
        }

        System.out.print("\nEnter new composition: ");
        String newComposition = sc.nextLine();

        scientist.refinePrototype(prototype, newComposition);
        repository.updatePrototype(prototype);
        System.out.println("Prototype refined successfully!");
        System.out.println("New version: " + prototype.getVersion());
        System.out.println("New composition: " + prototype.getComposition());
    }

    public void approvePrototype() {
        List<MaterialPrototype> prototypes = repository.getAllPrototypes();
        List<MaterialPrototype> testingPrototypes = new ArrayList<>();
        for (MaterialPrototype p : prototypes) {
            if (p.getStatus().equals("Testing")) {
                testingPrototypes.add(p);
            }
        }

        if (testingPrototypes.isEmpty()) {
            System.out.println("No prototypes in testing phase.");
            return;
        }

        System.out.println("\n=== PROTOTYPES IN TESTING ===");
        for (MaterialPrototype proto : testingPrototypes) {
            System.out.println(proto);
        }

        System.out.print("\nEnter prototype ID to approve/reject: ");
        int protoId = sc.nextInt();
        sc.nextLine();

        MaterialPrototype prototype = repository.findPrototypeById(protoId);
        if (prototype == null) {
            System.out.println("Prototype not found!");
            return;
        }

        prototype.displayDetails();

        // Calculate average rating from feedback
        if (!prototype.getFeedbackList().isEmpty()) {
            double avgRating = prototype.getFeedbackList().stream()
                .mapToInt(MaterialFeedback::getRating)
                .average()
                .orElse(0);
            System.out.println("\nAverage Rating: " + String.format("%.1f", avgRating) + "/5");
        }

        System.out.println("\n1. Approve for Production Testing");
        System.out.println("2. Reject Prototype");
        System.out.print("Choose option: ");
        int choice = sc.nextInt();

        if (choice == 1) {
            prototype.setStatus("Approved");
            repository.updatePrototype(prototype);
            System.out.println("Prototype approved for production testing!");
        } else if (choice == 2) {
            prototype.setStatus("Rejected");
            repository.updatePrototype(prototype);
            System.out.println("Prototype rejected.");
        }
    }

    public List<MaterialPrototype> getPrototypes() {
        return repository.getAllPrototypes();
    }

    public List<MaterialSpecification> getSpecifications() {
        return repository.getAllSpecifications();
    }
}
