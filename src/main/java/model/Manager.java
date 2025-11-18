package model;

import java.util.ArrayList;
import java.util.List;

public class Manager extends Staff {

    private List<Staff> employees = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>(); //All created tasks

    public Manager (String n, String d, String r, int s) {

        super(n, d, r, s);

    }

    public List<Staff> getAllEmployees() {
        return employees;
    }

    //Return list off all employees with < 3 in progress tasks
    public List<Staff> getAvailableEmployees() {
        
        List<Staff> availableList = new ArrayList<>();

        for (Staff s : employees) {
            if (s.isAvailable()) availableList.add(s);
        }

        return availableList;
    }

    public void addEmployee (Staff s) {
        employees.add(s);
    }

    @Override
    public void addTask (Task t) {
        tasks.add(t);
    }
    @Override
    public List<Task> getTasks() {
        return tasks;
    }
    @Override
    public void removeTask (Task t) {
        tasks.remove(t);
    }

    //Return list of tasks not assigned to any employee
    @Override
    public List<Task> getUnassignedTasks() {

        List<Task> unassigned = new ArrayList<>();
        for (Task t : tasks) {
            if (!t.isAssigned()) unassigned.add(t);
        }
        return unassigned;

    }

}