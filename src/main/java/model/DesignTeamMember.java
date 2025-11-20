package model;

public class DesignTeamMember extends Staff {

    public DesignTeamMember(String name, String department, String role, int salary) {
        super(name, department, role, salary);
    }

    public MaterialFeedback evaluateAesthetics(MaterialPrototype prototype, String comments, int rating) {
        return new MaterialFeedback(comments, rating, "Design", this.getName());
    }
}
