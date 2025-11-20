package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialPrototypeRepository {
    private final String specificationsFilePath;
    private final String prototypesFilePath;
    private final String feedbackFilePath;
    private final List<MaterialSpecification> specifications;
    private final List<MaterialPrototype> prototypes;
    private final List<MaterialFeedback> allFeedback;

    public MaterialPrototypeRepository(String specificationsFilePath, String prototypesFilePath, String feedbackFilePath) {
        this.specificationsFilePath = specificationsFilePath;
        this.prototypesFilePath = prototypesFilePath;
        this.feedbackFilePath = feedbackFilePath;
        this.specifications = new ArrayList<>();
        this.prototypes = new ArrayList<>();
        this.allFeedback = new ArrayList<>();
        loadFromFiles();
    }

    public List<MaterialSpecification> getAllSpecifications() {
        return new ArrayList<>(specifications);
    }

    public List<MaterialPrototype> getAllPrototypes() {
        return new ArrayList<>(prototypes);
    }

    public List<MaterialFeedback> getAllFeedback() {
        return new ArrayList<>(allFeedback);
    }

    public MaterialSpecification findSpecificationById(int id) {
        return specifications.stream()
                .filter(s -> s.getSpecID() == id)
                .findFirst()
                .orElse(null);
    }

    public MaterialPrototype findPrototypeById(int id) {
        return prototypes.stream()
                .filter(p -> p.getPrototypeID() == id)
                .findFirst()
                .orElse(null);
    }

    public void addSpecification(MaterialSpecification specification) {
        specifications.add(specification);
        saveSpecifications();
    }

    public void addPrototype(MaterialPrototype prototype) {
        prototypes.add(prototype);
        savePrototypes();
    }

    public void updatePrototype(MaterialPrototype prototype) {
        MaterialPrototype existing = findPrototypeById(prototype.getPrototypeID());
        if (existing != null) {
            prototypes.remove(existing);
            prototypes.add(prototype);
            savePrototypes();
        }
    }

    public void addFeedback(MaterialFeedback feedback) {
        allFeedback.add(feedback);
        saveFeedback();
    }

    private void loadFromFiles() {
        loadSpecifications();
        loadFeedback();
        loadPrototypes();
    }

    private void loadSpecifications() {
        File file = new File(specificationsFilePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                MaterialSpecification spec = MaterialSpecification.fromCSV(line);
                if (spec != null) {
                    specifications.add(spec);
                }
            }
            System.out.println("Loaded " + specifications.size() + " material specifications from file.");
        } catch (IOException e) {
            System.err.println("Error loading material specifications: " + e.getMessage());
        }
    }

    private void loadFeedback() {
        File file = new File(feedbackFilePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                MaterialFeedback feedback = MaterialFeedback.fromCSV(line);
                if (feedback != null) {
                    allFeedback.add(feedback);
                }
            }
            System.out.println("Loaded " + allFeedback.size() + " feedback entries from file.");
        } catch (IOException e) {
            System.err.println("Error loading feedback: " + e.getMessage());
        }
    }

    private void loadPrototypes() {
        File file = new File(prototypesFilePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                MaterialPrototype prototype = MaterialPrototype.fromCSV(line, specifications, allFeedback);
                if (prototype != null) {
                    prototypes.add(prototype);
                }
            }
            System.out.println("Loaded " + prototypes.size() + " prototypes from file.");
        } catch (IOException e) {
            System.err.println("Error loading prototypes: " + e.getMessage());
        }
    }

    private void saveSpecifications() {
        ensureParentDirectory(specificationsFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(specificationsFilePath))) {
            for (MaterialSpecification spec : specifications) {
                writer.write(spec.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving specifications: " + e.getMessage());
        }
    }

    private void savePrototypes() {
        ensureParentDirectory(prototypesFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(prototypesFilePath))) {
            for (MaterialPrototype prototype : prototypes) {
                writer.write(prototype.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving prototypes: " + e.getMessage());
        }
    }

    private void saveFeedback() {
        ensureParentDirectory(feedbackFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(feedbackFilePath))) {
            for (MaterialFeedback feedback : allFeedback) {
                writer.write(feedback.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving feedback: " + e.getMessage());
        }
    }

    private void ensureParentDirectory(String filename) {
        File f = new File(filename);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
}
