package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a marketing campaign.
 *
 * @author Sheldon Corkery
 */
public class Campaign {
    private final long id;
    private String name;
    private String targetCollection;
    private double budget;
    private LocalDate startDate;
    private LocalDate endDate;
    private CampaignStatus status;
    private List<String> logs;
    private String assetLinks;
    private String adCopyText;
    private String rejectionNotes;
    private double totalSalesRevenue;

    public enum CampaignStatus {
        DRAFT,
        PENDING_BUDGET,
        PENDING_CONTENT,
        PENDING_LEGAL,
        APPROVED,
        READY_TO_LAUNCH,
        ACTIVE,
        SUSPENDED,
        ENDED
    }

    public Campaign(long id, String name, String targetCollection, double budget,
                    LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.targetCollection = targetCollection;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = CampaignStatus.DRAFT;
        this.logs = new ArrayList<>();
        this.totalSalesRevenue = 0.0;
    }

    public void addLog(String logEntry) {
        logs.add(LocalDate.now() + ": " + logEntry);
    }

    public void setStatus(CampaignStatus campaignStatus) {
        this.status = campaignStatus;
        addLog("Status changed to " + campaignStatus);
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getTargetCollection() {
        return targetCollection;
    }

    public void setTargetCollection(String targetCollection) {
        this.targetCollection = targetCollection;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }

    public String getAssetLinks() {
        return assetLinks;
    }

    public void setAssetLinks(String assetLinks) {
        this.assetLinks = assetLinks;
    }

    public String getAdCopyText() {
        return adCopyText;
    }

    public void setAdCopyText(String adCopyText) {
        this.adCopyText = adCopyText;
    }

    public String getRejectionNotes() {
        return rejectionNotes;
    }

    public void setRejectionNotes(String rejectionNotes) {
        this.rejectionNotes = rejectionNotes;
    }

    public double getTotalSalesRevenue() {
        return totalSalesRevenue;
    }

    public void setTotalSalesRevenue(double totalSalesRevenue) {
        this.totalSalesRevenue = totalSalesRevenue;
    }

    public double calculateROI() {
        if (budget == 0) {
            return 0.0;
        }
        return totalSalesRevenue / budget;
    }

    @Override
    public String toString() {
        return String.format("Campaign[id=%d, name='%s', status=%s, budget=$%.2f, collection='%s']",
                id, name, status, budget, targetCollection);
    }
}
