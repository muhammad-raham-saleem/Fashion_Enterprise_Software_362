package service;

import util.LogManager;
import java.util.List;
import util.FileManager;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReportService {

    private final LogManager LogManager;

    public ReportService(LogManager LogManager) {
        this.LogManager = LogManager;
    }

    public void generateSalesReport(LocalDate date) {
        List<String> logLines = LogManager.readAllLines();

        if (logLines == null | logLines.isEmpty()){
            System.out.println("No sales data available for the report.");
            return;
        }
        System.out.println("\n--- SALES REPORT FOR " + date + " ---");

        List<String> dailySales = new ArrayList<>();
        double totalRevenue = 0.0;

        for (String line : logLines) {
            String[] parts = line.split(",");
            if (parts.length != 3) continue;
            LocalDate saleDate = LocalDate.parse(parts[0].trim());
            if (saleDate.equals(date)) {
                dailySales.add(line);
                totalRevenue += Double.parseDouble(parts[2].trim());
            }

            List<String> report = new ArrayList<>();
            report.add("Daily Sales Report for " + date);
            report.add("----------------------------");
            report.add("Total Sales: " + dailySales.size());
            report.add(String.format("Total Revenue: $%.2f", totalRevenue));
            report.add("\nDetailed Sales:");
            if (dailySales.isEmpty()){
                report.add("No sales recorded on this date.");
            }
            else {
                report.addAll(dailySales);
            }
            System.out.println(String.join("\n", report));
            String fileName = "data/SalesReports/Sales_report_" + date + ".txt";
            FileManager.writeLines(fileName, report);

            
        }
        System.out.println("Sales report generated");

        
        
    }

    
}
