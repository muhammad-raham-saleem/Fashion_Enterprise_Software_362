package model;

import java.util.ArrayList;
import java.util.List;

public class MaterialPrototype {
    private static int count = 1;

    private int prototypeID;
    private String name;
    private String status; // "In Development", "Testing", "Approved", "Rejected"
    private String composition;
    private int version;
    private MaterialSpecification specification;
    private List<MaterialFeedback> feedbackList;

    public MaterialPrototype(String name, MaterialSpecification specification) {
        this.prototypeID = count++;
        this.name = name;
        this.specification = specification;
        this.composition = "";
        this.version = 1;
        this.status = "In Development";
        this.feedbackList = new ArrayList<>();
    }

    public int getPrototypeID() {
        return prototypeID;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public int getVersion() {
        return version;
    }

    public void incrementVersion() {
        this.version++;
    }

    public MaterialSpecification getSpecification() {
        return specification;
    }

    public List<MaterialFeedback> getFeedbackList() {
        return feedbackList;
    }

    public void addFeedback(MaterialFeedback feedback) {
        this.feedbackList.add(feedback);
    }

    public void displayDetails() {
        System.out.println("\n=== PROTOTYPE DETAILS ===");
        System.out.println("Prototype ID: " + prototypeID);
        System.out.println("Name: " + name);
        System.out.println("Status: " + status);
        System.out.println("Version: " + version);
        System.out.println("Composition: " + composition);
        System.out.println("Specification: " + specification.toString());
        System.out.println("Feedback Count: " + feedbackList.size());
    }

    public String toCSV() {
        // Serialize feedback IDs only (feedback will be stored separately)
        String feedbackIds = feedbackList.stream()
                .map(f -> String.valueOf(f.getFeedbackID()))
                .reduce((a, b) -> a + ";" + b)
                .orElse("");

        return String.format("%d,%s,%s,%s,%d,%d,%s",
                prototypeID,
                escapeCsv(name),
                escapeCsv(status),
                escapeCsv(composition),
                version,
                specification.getSpecID(),
                feedbackIds);
    }

    public static MaterialPrototype fromCSV(String line, java.util.List<MaterialSpecification> specs,
                                            java.util.List<MaterialFeedback> allFeedback) {
        String[] parts = line.split(",", 7);
        if (parts.length < 7) return null;
        try {
            int id = Integer.parseInt(parts[0]);
            String name = unescapeCsv(parts[1]);
            String status = unescapeCsv(parts[2]);
            String composition = unescapeCsv(parts[3]);
            int version = Integer.parseInt(parts[4]);
            int specId = Integer.parseInt(parts[5]);
            String feedbackIdsStr = parts[6];

            // Find specification
            MaterialSpecification spec = specs.stream()
                    .filter(s -> s.getSpecID() == specId)
                    .findFirst()
                    .orElse(null);

            if (spec == null) {
                System.err.println("Warning: Specification not found for prototype " + id);
                return null;
            }

            MaterialPrototype prototype = new MaterialPrototype(name, spec);
            prototype.prototypeID = id;
            prototype.status = status;
            prototype.composition = composition;
            prototype.version = version;

            // Load feedback
            if (!feedbackIdsStr.isEmpty()) {
                String[] feedbackIds = feedbackIdsStr.split(";");
                for (String fidStr : feedbackIds) {
                    try {
                        int fid = Integer.parseInt(fidStr);
                        allFeedback.stream()
                                .filter(f -> f.getFeedbackID() == fid)
                                .findFirst()
                                .ifPresent(prototype.feedbackList::add);
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            // Update static counter
            if (id >= count) {
                count = id + 1;
            }

            return prototype;
        } catch (Exception e) {
            System.err.println("Error parsing MaterialPrototype from CSV: " + line);
            return null;
        }
    }

    private static String escapeCsv(String value) {
        if (value == null || value.isEmpty()) return "";
        return value.replace(",", "\\,").replace("\n", "\\n");
    }

    private static String unescapeCsv(String value) {
        if (value == null || value.isEmpty()) return "";
        return value.replace("\\,", ",").replace("\\n", "\n");
    }

    @Override
    public String toString() {
        return "Prototype ID: " + prototypeID + ", Name: " + name +
               ", Status: " + status + ", Version: " + version;
    }
}
