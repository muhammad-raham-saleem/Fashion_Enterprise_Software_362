package model;

public class Material {
    private String name;
    private boolean available;

    public Material(String name, boolean available) {
        this.name = name;
        this.available = available;
    }

    public String getName() { return name; }
    public boolean isAvailable() { return available; }

    @Override
    public String toString() {
        return name + " (Available: " + available + ")";
    }

    public String serialize() {
        return name + "," + available;
    }

    public static Material deserialize(String line) {
        String[] parts = line.split(",");
        if (parts.length < 2) return null;
        return new Material(parts[0], Boolean.parseBoolean(parts[1]));
    }
}
