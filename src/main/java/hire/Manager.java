package hire;

import java.util.ArrayList;
import java.util.List;

public class Manager {

    private String name;
    private String department;
    private int salary;
    private List<Staff> managees = new ArrayList<>();

    public Manager (String n, String d, int s) {
        this.name = n;
        this.department = d;
        this.salary = s;
    }

    public void addManagee (Staff s) {
        this.managees.add(s);
    }

}