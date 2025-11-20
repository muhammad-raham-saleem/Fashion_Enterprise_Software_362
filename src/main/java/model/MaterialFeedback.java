package model;

public class MaterialFeedback {
    private static int count = 1;

    private int feedbackID;
    private String comments;
    private int rating; // 1-5
    private String feedbackType; // "Manufacturing", "Design", "Quality"
    private String reviewerName;

    public MaterialFeedback(String comments, int rating, String feedbackType, String reviewerName) {
        this.feedbackID = count++;
        this.comments = comments;
        this.rating = Math.max(1, Math.min(5, rating)); // Ensure rating is between 1-5
        this.feedbackType = feedbackType;
        this.reviewerName = reviewerName;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public String getComments() {
        return comments;
    }

    public int getRating() {
        return rating;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String toCSV() {
        return String.format("%d,%s,%d,%s,%s",
                feedbackID,
                escapeCsv(comments),
                rating,
                escapeCsv(feedbackType),
                escapeCsv(reviewerName));
    }

    public static MaterialFeedback fromCSV(String line) {
        String[] parts = line.split(",", 5);
        if (parts.length < 5) return null;
        try {
            int id = Integer.parseInt(parts[0]);
            String comments = unescapeCsv(parts[1]);
            int rating = Integer.parseInt(parts[2]);
            String type = unescapeCsv(parts[3]);
            String reviewer = unescapeCsv(parts[4]);

            MaterialFeedback feedback = new MaterialFeedback(comments, rating, type, reviewer);
            feedback.feedbackID = id;

            // Update static counter
            if (id >= count) {
                count = id + 1;
            }

            return feedback;
        } catch (Exception e) {
            System.err.println("Error parsing MaterialFeedback from CSV: " + line);
            return null;
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        return value.replace(",", "\\,").replace("\n", "\\n");
    }

    private static String unescapeCsv(String value) {
        if (value == null) return "";
        return value.replace("\\,", ",").replace("\\n", "\n");
    }

    @Override
    public String toString() {
        return "Feedback ID: " + feedbackID + ", Type: " + feedbackType +
               ", Rating: " + rating + "/5, Reviewer: " + reviewerName +
               ", Comments: " + comments;
    }
}
