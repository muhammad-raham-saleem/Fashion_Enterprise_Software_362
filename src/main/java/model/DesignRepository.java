package model;

import java.util.ArrayList;
import java.util.List;

public class DesignRepository {
    private List<Sketch> sketches = new ArrayList<>();
    private List<Material> materials = new ArrayList<>();

    public void addSketch(Sketch s) { sketches.add(s); }
    public void addMaterial(Material m) { materials.add(m); }

    public List<Sketch> getSketches() { return sketches; }
    public List<Material> getMaterials() { return materials; }

    public void clearAll() {
        sketches.clear();
        materials.clear();
    }
}
