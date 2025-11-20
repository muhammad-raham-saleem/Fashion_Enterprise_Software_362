package model;

public class ManufacturingTechnician extends Staff {

    public ManufacturingTechnician(String name, String department, String role, int salary) {
        super(name, department, role, salary);
    }

    public MaterialFeedback testManufacturability(MaterialPrototype prototype, String comments, int rating) {
        return new MaterialFeedback(comments, rating, "Manufacturing", this.getName());
    }
}
