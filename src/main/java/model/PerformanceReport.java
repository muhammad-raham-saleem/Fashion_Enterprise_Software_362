package model;

import java.time.LocalDate;

/**
 * Represents the performance report for a campaign.
 *
 * @author Sheldon Corkery
 */
public class PerformanceReport {
    private final long campaignId;
    private final String campaignName;
    private final double budget;
    private final double totalSalesRevenue;
    private final double roi;
    private final LocalDate generatedDate;
    private final String summary;

    public PerformanceReport(Campaign campaign) {
        this.campaignId = campaign.getId();
        this.campaignName = campaign.getName();
        this.budget = campaign.getBudget();
        this.totalSalesRevenue = campaign.getTotalSalesRevenue();
        this.roi = campaign.calculateROI();
        this.generatedDate = LocalDate.now();
        this.summary = generateSummary();
    }

    private String generateSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== CAMPAIGN PERFORMANCE REPORT =====\n");
        sb.append("Campaign: ").append(campaignName).append("\n");
        sb.append("Budget Allocated: $").append(String.format("%.2f", budget)).append("\n");
        sb.append("Total Sales Revenue: $").append(String.format("%.2f", totalSalesRevenue)).append("\n");
        sb.append("Return on Investment (ROI): ").append(String.format("%.2fx", roi)).append("\n");
        sb.append("Net Profit/Loss: $").append(String.format("%.2f", totalSalesRevenue - budget)).append("\n");
        sb.append("Report Generated: ").append(generatedDate).append("\n");
        sb.append("======================================\n");

        if (roi > 2.0) {
            sb.append("Status: Excellent Performance ✓\n");
        } else if (roi > 1.0) {
            sb.append("Status: Profitable ✓\n");
        } else if (roi >= 0.8) {
            sb.append("Status: Break-even ~\n");
        } else {
            sb.append("Status: Needs Improvement ✗\n");
        }

        return sb.toString();
    }

    public long getCampaignId() {
        return campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public double getBudget() {
        return budget;
    }

    public double getTotalSalesRevenue() {
        return totalSalesRevenue;
    }

    public double getRoi() {
        return roi;
    }

    public LocalDate getGeneratedDate() {
        return generatedDate;
    }

    public String getSummary() {
        return summary;
    }

    @Override
    public String toString() {
        return summary;
    }
}

