package service;

import model.Campaign;

/**
 * Orchestrates campaign activities
 *
 * @author Sheldon Corkery
 */
public class CampaignService {

    private Campaign campaign;

    public void createCampaign() {
        campaign = new Campaign();
    }

    public void reviewBudget(String campaignId, boolean approved, String notes) {
        if (approved) {
            campaign.setStatus(Campaign.CampaignStatus.PENDING_CONTENT);
        } else {
            campaign.setStatus(Campaign.CampaignStatus.DRAFT);
            campaign.addLog(notes);
        }
    }

    // Called by System/Marketer for the report
    public String generateReport(String campaignId) {
        // logic to fetch sales revenue vs budget
    }
}
