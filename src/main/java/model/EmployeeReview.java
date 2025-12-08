package model;

import java.util.*;
import java.time.LocalDateTime;

public class EmployeeReview {

    public class Rating {

        private String category;
        private double score;
        private String comments;

        private Rating (String cat, double s, String comm) {
            category = cat;
            score = s;
            comments = comm;
        }

        public String getCategory() {
            return category;
        }
        public double getScore() {
            return score;
        }
        public String getComments() {
            return comments;
        }

    }

    private Manager reviewer;
    private Staff reviewee;
    private LocalDateTime dateTime;

    private Map<Task, List<Rating>> tasks;
    private String comments;

    private boolean approved;

    //Normal constructor
    public EmployeeReview (Manager m, Staff s) {
        reviewer = m;
        reviewee = s;
        dateTime = LocalDateTime.now();
        tasks = new HashMap<>();
        approved = false;
    }

    //Load from file constructor
    public EmployeeReview (Manager m, Staff s, LocalDateTime dt, boolean appr) {
        reviewer = m;
        reviewee = s;
        dateTime = dt;
        tasks = new HashMap<>();
        approved = appr;
    }

    //Getters
    public Manager getReviewer() {
        return reviewer;
    }
    public Staff getReviewee() {
        return reviewee;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public boolean isApproved() {
        return approved;
    }

    public Collection<Task> getTasks() {
        return tasks.keySet();
    }
    public List<Rating> getRatings (Task t) {
        return tasks.get(t);
    }
    public String getComments() {
        return comments;
    }


    public void addTask (Task t) {
        tasks.put(t, new ArrayList<>());
    }

    public void rateTask (Task t, String cat, double score, String comm) {
        
        Rating newRating = new Rating(cat, score, comm);
        tasks.get(t).add(newRating);

    }

    public void addComments (String comms) {
        comments = comms;
    }

    public void approve() {
        approved = true;
    }

}
