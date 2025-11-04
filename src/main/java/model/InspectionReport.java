package model;

import java.util.List;

public class InspectionReport {
    public enum Status {
        PENDING,
        PASSED,
        REJECTED
    }

    private long id;
    private ManufacturingBatch batch;
    private List<InspectionFinding> findings;
    private Status reportStatus;

    public InspectionReport(long id, ManufacturingBatch batch, List<InspectionFinding> findings, Status status) {
        this.id = id;
        this.batch = batch;
        this.findings = findings;
        this.reportStatus = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Inspection Report ID: ").append(id).append("\n");
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
