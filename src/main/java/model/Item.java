package model;

/**
 * Represents an individual item in the manufacturing system.
 * @author Sheldon Corkery
 */
public class Item {
    private int id;
    private Product product;
    private InspectionFinding.ProblemSeverity problemSeverity;

    public Item(int id, Product product) {
        this.id = id;
        this.product = product;
        problemSeverity = InspectionFinding.ProblemSeverity.NONE;
    }

    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public InspectionFinding.ProblemSeverity getProblemSeverity() {
        return problemSeverity;
    }

    public void setProblemSeverity(InspectionFinding.ProblemSeverity problemSeverity) {
        this.problemSeverity = problemSeverity;
    }
}
