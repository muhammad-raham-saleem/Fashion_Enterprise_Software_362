package service;

import model.Campaign;
import model.Campaign.CampaignStatus;
import model.CampaignRepository;
import model.PerformanceReport;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Orchestrates campaign activities
 *
 * @author Sheldon Corkery
 */
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final Map<Long, PerformanceReport> performanceReports;
    private final Scanner sc;
    private long nextCampaignId;

    public CampaignService(Scanner sc) {
        this.campaignRepository = new CampaignRepository("data/campaigns.csv");
        this.performanceReports = new HashMap<>();
        this.sc = sc;

        // Set nextCampaignId based on existing campaigns
        List<Campaign> existing = campaignRepository.getAll();
        this.nextCampaignId = existing.stream()
                .mapToLong(Campaign::getId)
                .max()
                .orElse(0L) + 1;
    }

    /**
     * Marketing Manager creates a new campaign
     */
    public Campaign createCampaign(String name, String targetCollection, double budget,
                                   LocalDate startDate, LocalDate endDate) {
        Campaign campaign = new Campaign(nextCampaignId++, name, targetCollection, budget, startDate, endDate);
        campaignRepository.add(campaign);
        campaign.addLog("Campaign created by Marketing Manager");
        System.out.println("Campaign created successfully: " + campaign);
        return campaign;
    }

    /**
     * Marketing Manager submits campaign for budget approval
     */
    public void submitForBudgetApproval(long campaignId) {
        Campaign campaign = campaignRepository.findById((int) campaignId);
        if (campaign == null) {
            System.out.println("Campaign not found");
            return;
        }
        if (campaign.getStatus() != CampaignStatus.DRAFT) {
            System.out.println("Campaign must be in DRAFT status. Current status: " + campaign.getStatus());
            return;
        }
        campaign.setStatus(CampaignStatus.PENDING_BUDGET);
        campaignRepository.update(campaign);
        System.out.println("Campaign submitted for budget approval");
    }

    /**
     * Finance Manager approves the budget - advances to next step
     */
    public void approveBudget(long campaignId) {
        Campaign campaign = campaignRepository.findById((int) campaignId);
        if (campaign == null) {
            System.out.println("Campaign not found");
            return;
        }

        System.out.println("Current Status: " + campaign.getStatus());

        if (campaign.getStatus() != CampaignStatus.PENDING_BUDGET) {
            System.out.println("Campaign is not pending budget approval");
            return;
        }

        campaign.setStatus(CampaignStatus.PENDING_CONTENT);
        campaign.addLog("Budget approved by Finance Manager");
        campaignRepository.update(campaign);
        System.out.println("Budget approved! Budget field locked at $" + campaign.getBudget());
        System.out.println("Campaign advanced to PENDING_CONTENT");
    }

    /**
     * Finance Manager rejects the budget with notes
     */
    public void rejectBudget(long campaignId, String notes) {
        Campaign campaign = campaignRepository.findById((int) campaignId);
        if (campaign == null) {
            System.out.println("Campaign not found");
            return;
        }
        campaign.setStatus(CampaignStatus.DRAFT);
        campaign.setRejectionNotes(notes);
        campaign.addLog("Budget rejected by Finance Manager: " + notes);
        campaignRepository.update(campaign);
        System.out.println("Budget rejected. Reason: " + notes);
        System.out.println("  Campaign returned to DRAFT");
    }

    /**
     * Marketing Manager uploads assets and ad copy
     */
    public void uploadAssets(long campaignId, String assetLinks, String adCopyText) {
        Campaign campaign = campaignRepository.findById((int) campaignId);
        if (campaign == null) {
            System.out.println("Campaign not found");
            return;
        }
        if (campaign.getStatus() != CampaignStatus.PENDING_CONTENT) {
            System.out.println("Campaign must have budget approved first");
            return;
        }
        campaign.setAssetLinks(assetLinks);
        campaign.setAdCopyText(adCopyText);
        campaign.addLog("Assets uploaded: " + assetLinks);
        campaignRepository.update(campaign);
        System.out.println("Assets and ad copy uploaded successfully");
    }

    /**
     * Marketing Manager submits campaign for legal review
     */
    public void submitForLegalReview(long campaignId) {
        Campaign campaign = campaignRepository.findById((int) campaignId);
        if (campaign == null) {
            System.out.println("Campaign not found");
            return;
        }
        if (campaign.getStatus() != CampaignStatus.PENDING_CONTENT) {
            System.out.println("Campaign must have assets uploaded first");
            return;
        }
        if (campaign.getAssetLinks() == null || campaign.getAdCopyText() == null) {
            System.out.println("Must upload assets and ad copy before legal review");
            return;
        }
        campaign.setStatus(CampaignStatus.PENDING_LEGAL);
        campaignRepository.update(campaign);
        System.out.println("Campaign submitted for legal review");
    }

    /**
     * Legal Counsel approves compliance - advances to approved
     */
    public void approveLegal(long campaignId) {
        Campaign campaign = campaignRepository.findById((int) campaignId);
        if (campaign == null) {
            System.out.println("Campaign not found");
            return;
        }

        System.out.println("Current Status: " + campaign.getStatus());

        if (campaign.getStatus() != CampaignStatus.PENDING_LEGAL) {
            System.out.println("Campaign is not pending legal review");
            return;
        }

        campaign.setStatus(CampaignStatus.APPROVED);
        campaign.addLog("Legal compliance approved by Legal Counsel");
        campaignRepository.update(campaign);
        System.out.println("Legal approved!");
        System.out.println("Campaign advanced to APPROVED");
    }

    /**
     * Legal Counsel requests changes to content
     */
    public void requestLegalChanges(long campaignId, String complianceNotes) {
        Campaign campaign = campaignRepository.findById((int) campaignId);
        if (campaign == null) {
            System.out.println("Campaign not found");
            return;
        }
        campaign.setStatus(CampaignStatus.PENDING_CONTENT);
        campaign.setRejectionNotes(complianceNotes);
        campaign.addLog("Legal changes requested: " + complianceNotes);
        campaignRepository.update(campaign);
        System.out.println("Legal changes requested: " + complianceNotes);
        System.out.println("  Campaign returned to PENDING_CONTENT");
    }

    /**
     * Marketing Manager schedules the campaign launch
     */
    public void scheduleLaunch(long campaignId) {
        Campaign campaign = campaignRepository.findById((int) campaignId);
        if (campaign == null) {
            System.out.println("Campaign not found");
            return;
        }
        if (campaign.getStatus() != CampaignStatus.APPROVED && campaign.getStatus() != CampaignStatus.READY_TO_LAUNCH) {
            System.out.println("Campaign must be approved first. Current status: " + campaign.getStatus());
            return;
        }

        LocalDate today = LocalDate.now();
        if (today.isBefore(campaign.getStartDate())) {
            System.out.println("Cannot launch before start date: " + campaign.getStartDate());
            System.out.println("  Today: " + today);
            return;
        }

        campaign.setStatus(CampaignStatus.ACTIVE);
        campaign.addLog("Campaign launched!");
        campaignRepository.update(campaign);
        System.out.println("Campaign is now ACTIVE!");
        System.out.println("  Running from " + campaign.getStartDate() + " to " + campaign.getEndDate());
    }

    /**
     * System tracks sales revenue during active campaign
     */
    public void trackSales(long campaignId, double salesRevenue) {
        Campaign campaign = campaignRepository.findById((int) campaignId);
        if (campaign == null) {
            System.out.println("Campaign not found");
            return;
        }
        if (campaign.getStatus() != CampaignStatus.ACTIVE) {
            System.out.println("Warning: Campaign is not active. Current status: " + campaign.getStatus());
        }

        double previousRevenue = campaign.getTotalSalesRevenue();
        campaign.setTotalSalesRevenue(previousRevenue + salesRevenue);
        campaign.addLog("Sales tracked: $" + String.format("%.2f", salesRevenue) +
                " (Total: $" + String.format("%.2f", campaign.getTotalSalesRevenue()) + ")");
        campaignRepository.update(campaign);
        System.out.println("Sales tracked: $" + String.format("%.2f", salesRevenue));
        System.out.println("  Total sales revenue: $" + String.format("%.2f", campaign.getTotalSalesRevenue()));
    }

    /**
     * Marketing Manager generates performance report
     */
    public PerformanceReport generatePerformanceReport(long campaignId) {
        Campaign campaign = campaignRepository.findById((int) campaignId);
        if (campaign == null) {
            System.out.println("Campaign not found");
            return null;
        }

        LocalDate today = LocalDate.now();
        if (campaign.getStatus() == CampaignStatus.ACTIVE && today.isBefore(campaign.getEndDate())) {
            System.out.println("Warning: Campaign is still active. Report will show current progress.");
        }

        PerformanceReport report = new PerformanceReport(campaign);
        performanceReports.put(campaignId, report);
        campaign.addLog("Performance report generated");
        campaignRepository.update(campaign);

        System.out.println(report.getSummary());
        return report;
    }

    /**
     * Marketing Manager closes the campaign
     */
    public void closeCampaign(long campaignId) {
        Campaign campaign = campaignRepository.findById((int) campaignId);
        if (campaign == null) {
            System.out.println("Campaign not found");
            return;
        }

        // Generate final report if not already done
        if (!performanceReports.containsKey(campaignId)) {
            generatePerformanceReport(campaignId);
        }

        campaign.setStatus(CampaignStatus.ENDED);
        campaign.addLog("Campaign closed by Marketing Manager");
        campaignRepository.update(campaign);
        System.out.println("Campaign closed successfully");
        System.out.println("  Final Status: ENDED");
    }

    /**
     * Update campaign state directly
     */
    public void updateCampaignState(long campaignId, CampaignStatus newStatus) {
        Campaign campaign = campaignRepository.findById((int) campaignId);
        if (campaign == null) {
            System.out.println("Campaign not found");
            return;
        }

        CampaignStatus oldStatus = campaign.getStatus();
        campaign.setStatus(newStatus);
        campaignRepository.update(campaign);
        System.out.println("Campaign state updated: " + oldStatus + " -> " + newStatus);
    }

    public List<Campaign> getAllCampaigns() {
        return campaignRepository.getAll();
    }

    public Campaign getCampaign(long id) {
        return campaignRepository.findById((int) id);
    }

    public void viewCampaignDetails(long campaignId) {
        Campaign campaign = campaignRepository.findById((int) campaignId);
        if (campaign == null) {
            System.out.println("Campaign not found");
            return;
        }

        System.out.println("\n========== CAMPAIGN DETAILS ==========");
        System.out.println("ID: " + campaign.getId());
        System.out.println("Name: " + campaign.getName());
        System.out.println("Target Collection: " + campaign.getTargetCollection());
        System.out.println("Budget: $" + String.format("%.2f", campaign.getBudget()));
        System.out.println("Start Date: " + campaign.getStartDate());
        System.out.println("End Date: " + campaign.getEndDate());
        System.out.println("Status: " + campaign.getStatus());
        System.out.println("Total Sales Revenue: $" + String.format("%.2f", campaign.getTotalSalesRevenue()));
        System.out.println("Current ROI: " + String.format("%.2fx", campaign.calculateROI()));

        if (campaign.getAssetLinks() != null) {
            System.out.println("Asset Links: " + campaign.getAssetLinks());
        }
        if (campaign.getAdCopyText() != null) {
            System.out.println("Ad Copy: " + campaign.getAdCopyText());
        }
        if (campaign.getRejectionNotes() != null) {
            System.out.println("Rejection Notes: " + campaign.getRejectionNotes());
        }

        System.out.println("\n--- Activity Log ---");
        for (String log : campaign.getLogs()) {
            System.out.println("  " + log);
        }
        System.out.println("=====================================\n");
    }

    public String getBudget(long id) {
        Campaign campaign = campaignRepository.findById((int) id);
        if (campaign == null) {
            return "Campaign not found";
        }
        return String.format("%.2f", campaign.getBudget());
    }
}
