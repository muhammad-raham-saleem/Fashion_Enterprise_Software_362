package model;

/**
 * Represents an inspection finding in the manufacturing system.
 * Used to document issues with Item (s).
 * @author Sheldon Corkery
 */
public class InspectionFinding {
    public enum Quality {
        PERFECT,
        MINOR,
        MAJOR,
        CRITICAL
    }

}
