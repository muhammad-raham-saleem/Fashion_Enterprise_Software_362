package model;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CampaignRepository implements Repository<Campaign> {
    private final String filePath;
    private final List<Campaign> campaigns;

    public CampaignRepository(String filePath) {
        this.filePath = filePath;
        this.campaigns = new ArrayList<>();
        loadFromFile();
    }

    @Override
    public List<Campaign> getAll() {
        return new ArrayList<>(campaigns);
    }

    @Override
    public Campaign findById(int id) {
        return campaigns.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Campaign findByName(String name) {
        return campaigns.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void add(Campaign campaign) {
        campaigns.add(campaign);
        saveToFile();
    }

    public void update(Campaign campaign) {
        Campaign existing = findById((int) campaign.getId());
        if (existing != null) {
            campaigns.remove(existing);
            campaigns.add(campaign);
            saveToFile();
        }
    }

    public void remove(Campaign campaign) {
        campaigns.remove(campaign);
        saveToFile();
    }

    @Override
    public Campaign parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 6) return null;

        try {
            if (parts[0].startsWith("id")) {
                return null; // Skip header line
            }
            long id = Long.parseLong(parts[0]);
            String name = parts[1];
            String targetCollection = parts[2];
            double budget = Double.parseDouble(parts[3]);
            LocalDate startDate = LocalDate.parse(parts[4]);
            LocalDate endDate = LocalDate.parse(parts[5]);

            Campaign campaign = new Campaign(id, name, targetCollection, budget, startDate, endDate);

            if (parts.length > 6 && !parts[6].isEmpty()) {
                campaign.setStatus(Campaign.CampaignStatus.valueOf(parts[6]));
            }
            if (parts.length > 7 && !parts[7].isEmpty()) {
                campaign.setAssetLinks(parts[7]);
            }
            if (parts.length > 8 && !parts[8].isEmpty()) {
                campaign.setAdCopyText(parts[8]);
            }
            if (parts.length > 9 && !parts[9].isEmpty()) {
                campaign.setTotalSalesRevenue(Double.parseDouble(parts[9]));
            }

            return campaign;
        } catch (Exception e) {
            System.err.println("Error parsing campaign line: " + line);
            return null;
        }
    }

    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Campaign campaign = parseLine(line);
                if (campaign != null) {
                    campaigns.add(campaign);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading campaigns: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Campaign campaign : campaigns) {
                String line = String.format("%d,%s,%s,%.2f,%s,%s,%s,%s,%s,%.2f",
                        campaign.getId(),
                        campaign.getName(),
                        campaign.getTargetCollection(),
                        campaign.getBudget(),
                        campaign.getStartDate(),
                        campaign.getEndDate(),
                        campaign.getStatus(),
                        campaign.getAssetLinks() != null ? campaign.getAssetLinks() : "",
                        campaign.getAdCopyText() != null ? campaign.getAdCopyText() : "",
                        campaign.getTotalSalesRevenue()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving campaigns: " + e.getMessage());
        }
    }
}

