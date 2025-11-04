package model;

public class Sketch {
    private String name;
    private String theme;
    private boolean approved;

    public Sketch(String name, String theme, boolean approved) {
        this.name = name;
        this.theme = theme;
        this.approved = approved;
    }

    public String getName() { return name; }
    public String getTheme() { return theme; }
    public boolean isApproved() { return approved; }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return name + " (Theme: " + theme + ", Approved: " + approved + ")";
    }

    public String serialize() {
        return name + "," + theme + "," + approved;
    }

    public static Sketch deserialize(String line) {
        String[] parts = line.split(",");
        if (parts.length < 3) return null;
        return new Sketch(parts[0], parts[1], Boolean.parseBoolean(parts[2]));
    }
}
