package model;

/**
 * Represents an inspection finding in the manufacturing system.
 * Used to document issues with Item (s).
 * @author Sheldon Corkery
 */
public class InspectionFinding {
    public enum ProblemSeverity {
        NONE,
        MINOR,
        MAJOR
    }

    private final Item item;
    private ProblemSeverity severity;
    private String description;

    public InspectionFinding(Item item, ProblemSeverity severity, String description) {
        this.item = item;
        this.severity = severity;
        this.description = description;
    }

    public int getItemId() {
        return item.getId();
    }

    public InspectionFinding.ProblemSeverity getSeverity() {
        return severity;
    }

    public String getDescription() {
        return description;
    }

}
