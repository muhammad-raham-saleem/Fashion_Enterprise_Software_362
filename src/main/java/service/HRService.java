package service;

import model.*;
import java.util.Scanner;

public class HRService {

    private HR hr;
    private Scanner sc;

    public HRService (HR hr, Scanner sc) {
        this.hr = hr;
        this.sc = sc;
    }

    public void viewStaffList() {

        if (this.hr.getStaff().isEmpty()) System.out.println("No staff hired.");
        else this.hr.getStaff().forEach(System.out::println);
        
    }

    public void hireStaff () {

        System.out.println("Enter employee name:");
        String name = sc.nextLine();
        System.out.println("Enter name of department " + name + " will be working in:");
        String department = sc.nextLine();
        System.out.println("Enter" + name + "'s role:");
        String role = sc.nextLine();
        System.out.println("Enter" + name + "'s salary:");
        int salary = sc.nextInt();

        Staff newStaff;
        if (role.toLowerCase().contains("manager")) {
            newStaff = new Manager(name, department, role, salary);
        } else {
            newStaff = new Staff(name, department, role, salary);
        }
        hr.getStaff().add(newStaff);

    }

}