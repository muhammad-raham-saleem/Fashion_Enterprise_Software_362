package hire;

public class Staff {

    private String name;
    private String department;
    private int salary;
    private Manager manager;

    public Staff (String n, String d, int s) {
        this.name = n;
        this.department = d;
        this.salary = s;
    }

    public void setManager(Manager m) {
        this.manager = m;
    }

}