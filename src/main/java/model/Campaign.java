package model;

/**
 * Represents a marketing campaign.
 *
 * @author Sheldon Corkery
 */
public class Campaign {
    private final long id;
    private String name;
    private double budget;
    private CampaignStatus status;

    public enum CampaignStatus {
        DRAFT,
        PENDING_BUDGET,
        PENDING_CONTENT,
        PENDING_LEGAL,
        APPROVED,
        PENDING_LOGISTICS,
        READY_TO_LAUNCH,
        ACTIVE,
        ENDED
    }

    public Campaign(long id, String name, double budget, CampaignStatus status) {
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.status = status;
    }

    public void setStatus(CampaignStatus campaignStatus) {
        this.status = campaignStatus;
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

}
