package service;

import model.InspectionFinding;
import model.InspectionReport;
import model.Item;
import model.ManufacturingBatch;

import java.util.List;
import java.util.Scanner;

/**
 * Service class to handle quality control operations for manufacturing batches.
 * @author Sheldon Corkery
 */
public class QualityControlService {
    private final Scanner sc = new Scanner(System.in);
    private ManufacturingBatch batch;

    public QualityControlService(ManufacturingBatch batch) {
        this.batch = batch;
    }

    public void performBatchInspection() {
        List<Item> sampleItems = batch.startInspection(0.2);
        InspectionReport report = batch.getReport();
        for (Item item : sampleItems) {
            System.out.println("Inspecting Item ID: " + item.getId());
            System.out.println("Choose damage of item: ");
            System.out.println("0 - None, 1 - Minor, 2 - Major");
            int input = sc.nextInt();
            switch (input) {
                case 0 -> item.setProblemSeverity(InspectionFinding.ProblemSeverity.NONE);
                case 1 -> item.setProblemSeverity(InspectionFinding.ProblemSeverity.MINOR);
                case 2 -> item.setProblemSeverity(InspectionFinding.ProblemSeverity.MAJOR);
                default -> {
                    System.out.println("Invalid input, setting severity to NONE.");
                    item.setProblemSeverity(InspectionFinding.ProblemSeverity.NONE);
                }
            }
            if (item.getProblemSeverity() != InspectionFinding.ProblemSeverity.NONE) {
                System.out.print("Item failed specification. Enter description of problem: ");
                String description = sc.nextLine();
                InspectionFinding finding = new InspectionFinding(item, InspectionFinding.ProblemSeverity.MINOR, description);
                report.addFinding(finding);
            } else {
                System.out.println("Item passed.");
            }
        }
        if (report.getFailureRate() > 0.1) {
            System.out.println("Batch failed inspection.");
            batch.endInspection(InspectionReport.Status.REJECTED);
        } else {
            System.out.println("Batch passed inspection.");
            batch.endInspection(InspectionReport.Status.PASSED);
        }
    }
}
