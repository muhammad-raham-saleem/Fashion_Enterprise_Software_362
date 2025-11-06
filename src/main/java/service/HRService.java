package service;

import model.*;
import java.util.*;
import util.FileManager;

public class HRService {

    private HR hr;
    private Scanner sc;
    private String filename;

    public HRService (HR hr, Scanner sc, String f) {
        this.hr = hr;
        this.sc = sc;
        this.filename = f;
    }

    public void viewStaffList() {

        loadStaff();
        if (this.hr.getStaff().isEmpty()) System.out.println("No staff hired.");
        else {
            for (Staff s : this.hr.getStaff()) {
                System.out.println("Name: " + s.getName());
                System.out.println("Department: " + s.getDepartment());
                System.out.println("Role: " + s.getRole());
                System.out.println("Salary: " + s.getSalary());
            }
        }
        
    }

    public Staff createStaff (String name, String dep, String role, int salary) {

        Staff newStaff;
        if (role.toLowerCase().contains("manager")) {
            newStaff = new Manager(name, dep, role, salary);
        } else {
            newStaff = new Staff(name, dep, role, salary);
        }
        hr.getStaff().add(newStaff);
        return newStaff;

    }

    public void hireStaff () {

        sc.nextLine();
        System.out.println("Enter employee name:");
        String name = sc.nextLine();
        System.out.println("Enter name of department " + name + " will be working in:");
        String department = sc.nextLine();
        System.out.println("Enter " + name + "'s role:");
        String role = sc.nextLine();
        System.out.println("Enter " + name + "'s salary:");
        int salary = sc.nextInt();

        createStaff(name, department, role, salary);
        saveStaff();

    }

    private void saveStaff() {
        List<String> lines = new ArrayList<>();
        lines.add("name, department, role, salary");

        for (Staff s : this.hr.getStaff()){
            lines.add(s.getName() + ", " + s.getDepartment() + ", " + s.getRole() + ", " + s.getSalary());
        }
        FileManager.writeLines(filename, lines);
    }

    private void loadStaff() {
        List<String> lines = util.FileManager.readLines(filename);

        for (String line : lines){
            if (line.startsWith("name, ")) continue;

            String [] parts = line.split(", ");
            String name = parts[0];
            String dep = parts[1];
            String role = parts[2];
            int salary = Integer.parseInt(parts[3]);

            createStaff(name, dep, role, salary); 
        } 
    }

}