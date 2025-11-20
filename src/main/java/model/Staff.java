package model;

import java.util.*;

public class Staff {

    private static int count = 1;

    private int staff_id;
    private String name;
    private String department;
    private String role;
    private int salary;

    private Manager manager; //Manager assigned to this staff member
    private List<Task> tasks = new ArrayList<>(); //Current assigned tasks
    private boolean available; //Is the staff member available to be assigned tasks?

    public Staff (String n, String d, String r, int s) {

        name = n;
        department = d;
        role = r;
        salary = s;
        available = true;

        //Assign unique staff ID
        staff_id = count;
        count++;

    }

    public String getName() {
        return name;
    }
    public int getID() {
        return staff_id;
    }
    public String getDepartment() {
        return department;
    }
    public String getRole() {
        return role;
    }
    public int getSalary() {
        return salary;
    }
    public boolean isAvailable() {
        return available;
    }


    public void setManager (Manager m) {
        manager = m;
    }

    public List<Task> getTasks() {
        return tasks;
    }
    public void addTask (Task t) {
        tasks.add(t);
        if (tasks.size() == 3) available = false;
    }
    public void removeTask (Task t) {
        tasks.remove(t);
        available = true;
    }

    //Return list of unassigned tasks
    public List<Task> getUnassignedTasks() {

        List<Task> unaccepted = new ArrayList<>();
        for (Task t : tasks) {
            if (!t.isAssigned() && !t.isCompleted()) unaccepted.add(t);
        }
        return unaccepted;

    }

    //Return list of tasks assigned, but not accepted
    public List<Task> getUnrespondedTasks() {

        List<Task> unaccepted = new ArrayList<>();
        for (Task t : tasks) {
            if (t.isAssigned() && !t.isAccepted()) unaccepted.add(t);
        }
        return unaccepted;

    }

    //Return list of tasks accepted, but not completed
    public List<Task> getInProgressTasks() {

        List<Task> progress = new ArrayList<>();
        for (Task t : tasks) {
            if (t.isAccepted() && !t.isCompleted()) progress.add(t);
        }
        return progress;

    }

}