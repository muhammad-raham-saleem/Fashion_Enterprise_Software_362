package model;

import java.util.*;

public class Staff {

    private static int count = 0;

    private int staff_id;
    private String name;
    private String department;
    private String role;
    private int salary;

    private Manager manager; //Manager assigned to this staff member
    private List<Task> assignedTasks = new ArrayList<>(); //Current assigned tasks
    private boolean available; //Is the staff member available to be assigned tasks?

    public Staff (String n, String d, String r, int s) {

        this.name = n;
        this.department = d;
        this.role = r;
        this.salary = s;

        //Assign unique staff ID
        staff_id = count;
        count++;

    }

    public String getName() {
        return this.name;
    }
    public int getID() {
        return staff_id;
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
    public boolean isAvailable() {
        return available;
    }


    public void setManager (Manager m) {
        manager = m;
    }
    public void addTask (Task t) {
        assignedTasks.add(t);
    }

}