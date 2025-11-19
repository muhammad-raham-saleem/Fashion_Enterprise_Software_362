package model;

import java.util.List;
import java.util.Scanner;

public class Schedule {

    public void generateSchedule(List<String> products) {
        System.out.println("Generating schedule for items:");
        for (String product : products) {
            System.out.println(" - " + product);
        }
    }

    public boolean approveSchedule() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Approve schedule for show? (Y/N)");
        String answer = scan.nextLine();
        if (answer.equalsIgnoreCase("Y")){
            return true;
        }
        else{
            return false;
            
        }
        
    }
}
