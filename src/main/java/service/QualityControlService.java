package service;

import model.InspectionFinding;
import model.InspectionReport;
import model.Item;
import model.ManufacturingBatch;

import java.util.List;

/**
 * Service class to handle quality control operations for manufacturing batches.
 * @author Sheldon Corkery
 */
public class QualityControlService {
    private ManufacturingBatch batch;
    private List<InspectionFinding> findings;

    public QualityControlService(ManufacturingBatch batch) {
        this.batch = batch;
    }

    public void performBatchInspection() {
        List<Item> sampleItems = batch.startInspection(0.2);
        for (Item item : sampleItems) {
            item.validateQuality();
        }
        batch.endInspection(InspectionReport.Status.PASSED);
    }
}
