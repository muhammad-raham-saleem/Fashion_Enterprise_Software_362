package model;

import java.util.ArrayList;
import java.util.List;

public class Manager extends Staff {

    private List<Staff> employees = new ArrayList<>();
    private List<Task> createdTasks = new ArrayList<>();

    public Manager (String n, String d, String r, int s) {

        super(n, d, r, s);

    }

    public List<Staff> getAllEmployees() {
        return employees;
    }

    //Return list off all employees with < 3 in progress tasks
    public List<Staff> getAvailableEmployees() {
        
        List<Staff> available = new ArrayList<>();

        for (Staff s : employees) {
            if (s.isAvailable()) available.add(s);
        }

        return available;
    }

    @Override
    public void addTask (Task t) {
        createdTasks.add(t);
    }

}