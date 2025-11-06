package util;

import model.Material;
import model.Sketch;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public static void saveData(List<Sketch> sketches, List<Material> materials) {
        try (PrintWriter writer = new PrintWriter("data/data.txt")) {
            writer.println("[SKETCHES]");
            for (Sketch s : sketches) writer.println(s.serialize());
            writer.println("[MATERIALS]");
            for (Material m : materials) writer.println(m.serialize());
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static void loadData(List<Sketch> sketches, List<Material> materials) {
        sketches.clear();
        materials.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/data.txt"))) {
            String line;
            boolean readingSketches = false, readingMaterials = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("[SKETCHES]")) {
                    readingSketches = true; readingMaterials = false;
                } else if (line.equals("[MATERIALS]")) {
                    readingSketches = false; readingMaterials = true;
                } else if (!line.isBlank()) {
                    if (readingSketches) {
                        Sketch s = Sketch.deserialize(line);
                        if (s != null) sketches.add(s);
                    } else if (readingMaterials) {
                        Material m = Material.deserialize(line);
                        if (m != null) materials.add(m);
                    }
                }
            }
            System.out.println("Data loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved data found, starting fresh.");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    public static List<String> readLines(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        }
        return lines;
    }

    public static void writeLines(String filename, List<String> lines) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + filename);
        }
    }
}
