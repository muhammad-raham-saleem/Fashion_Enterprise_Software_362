package model;

public class FinanceManager extends Staff {

    public FinanceManager(String name, String department, String role, int salary) {
        super(name, department, role, salary);
    }

    public boolean reviewBudget(VendorContract contract, double availableBudget) {
        return contract.getBudgetAmount() <= availableBudget;
    }

    public void addFinanceNotes(VendorContract contract, String notes) {
        contract.setFinanceNotes(notes);
    }
}
