package model;

import util.FileManager;

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

    private ManufacturingBatch batch;
    private List<InspectionFinding> findings;
    private Status reportStatus = Status.PENDING;

    public InspectionReport(ManufacturingBatch batch, List<InspectionFinding> findings) {
        this.batch = batch;
        this.findings = findings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
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
        StringBuilder sb = new StringBuilder();
        sb.append(batch.getId()).append(",")
                .append(batch.getProduct().getId()).append(",")
                .append(batch.countItems()).append(",")
                .append(reportStatus.toString());
        FileManager.updateLine("data/batches.csv", String.valueOf(batch.getId()), sb.toString());
        return true;
    }

    public boolean addFinding(InspectionFinding finding) {
        if (reportStatus != Status.PENDING) {
            return false;
        }
        return findings.add(finding);
    }

    /**
     * Calculate the failure rate based on the findings.
     * @return Failure rate as a double between 0.0 and 1.0
     */
    public double getFailureRate() {
        if (findings.isEmpty()) {
            return 0.0;
        }
        int failedCount = findings.size();
        for (InspectionFinding finding : findings) {
            if (finding.getSeverity() == InspectionFinding.ProblemSeverity.NONE) {
                failedCount--;
            }
        }
        return (double) failedCount / findings.size();
    }

    public Status getReportStatus() {
        return reportStatus;
    }
}
