package model;

public class RDScientist extends Staff {

    public RDScientist(String name, String department, String role, int salary) {
        super(name, department, role, salary);
    }

    public void createPrototype(MaterialPrototype prototype, String composition) {
        prototype.setComposition(composition);
        prototype.setStatus("Testing");
    }

    public void refinePrototype(MaterialPrototype prototype, String newComposition) {
        prototype.setComposition(newComposition);
        prototype.incrementVersion();
    }
}
