package model;

import java.util.List;

/**
 * Represents an inspection report for a manufacturing batch.
 * @author Sheldon Corkery
 */
public class InspectionReport {
    public enum Status {
        PENDING,
        PASSED,
        REJECTED
    }

    private int id;
    private ManufacturingBatch batch;
    private List<InspectionFinding> findings;
    private Status reportStatus = Status.PENDING;

    public InspectionReport(int id, ManufacturingBatch batch, List<InspectionFinding> findings) {
        this.id = id;
        this.batch = batch;
        this.findings = findings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Inspection Report ID: ").append(id).append("\n");
        sb.append("Batch: ").append(batch).append("\n");
        sb.append("Status: ").append(reportStatus).append("\n");
        sb.append("Findings:\n");
        for (InspectionFinding finding : findings) {
            sb.append("- ").append(finding).append("\n");
        }
        return sb.toString();
    }

    public boolean sealReport(Status status) {
        if (reportStatus != Status.PENDING) {
            return false;
        }

        this.reportStatus = status;
        return true;
    }

    public boolean addFinding(InspectionFinding finding) {
        if (reportStatus != Status.PENDING) {
            return false;
        }
        return findings.add(finding);
    }
}
