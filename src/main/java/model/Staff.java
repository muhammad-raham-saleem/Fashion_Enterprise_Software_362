package model;

public class Staff {

    private String name;
    private String department;
    private String role;
    private int salary;

    public Staff (String n, String d, String r, int s) {

        this.name = n;
        this.department = d;
        this.role = r;
        this.salary = s;

    }

    public String getName() {
        return this.name;
    }
    public String getDepartment() {
        return this.department;
    }
    public String getRole() {
        return this.role;
    }
    public int getSalary() {
        return this.salary;
    }

}