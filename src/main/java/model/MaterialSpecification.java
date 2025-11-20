package model;

public class MaterialSpecification {
    private static int count = 1;

    private int specID;
    private String texture;
    private double weight;
    private String sustainabilityReq;
    private String color;
    private String finish;

    public MaterialSpecification(String texture, double weight, String sustainabilityReq, String color, String finish) {
        this.specID = count++;
        this.texture = texture;
        this.weight = weight;
        this.sustainabilityReq = sustainabilityReq;
        this.color = color;
        this.finish = finish;
    }

    public int getSpecID() {
        return specID;
    }

    public String getTexture() {
        return texture;
    }

    public double getWeight() {
        return weight;
    }

    public String getSustainabilityReq() {
        return sustainabilityReq;
    }

    public String getColor() {
        return color;
    }

    public String getFinish() {
        return finish;
    }

    public String toCSV() {
        return String.format("%d,%s,%.2f,%s,%s,%s",
                specID,
                escapeCsv(texture),
                weight,
                escapeCsv(sustainabilityReq),
                escapeCsv(color),
                escapeCsv(finish));
    }

    public static MaterialSpecification fromCSV(String line) {
        String[] parts = line.split(",", 6);
        if (parts.length < 6) return null;
        try {
            int id = Integer.parseInt(parts[0]);
            String texture = unescapeCsv(parts[1]);
            double weight = Double.parseDouble(parts[2]);
            String sustainability = unescapeCsv(parts[3]);
            String color = unescapeCsv(parts[4]);
            String finish = unescapeCsv(parts[5]);

            MaterialSpecification spec = new MaterialSpecification(texture, weight, sustainability, color, finish);
            spec.specID = id;

            // Update static counter
            if (id >= count) {
                count = id + 1;
            }

            return spec;
        } catch (Exception e) {
            System.err.println("Error parsing MaterialSpecification from CSV: " + line);
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
        return "Texture: " + texture + ", Weight: " + weight + "g, Color: " + color +
               ", Finish: " + finish + ", Sustainability: " + sustainabilityReq;
    }
}
