package service;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import model.Events;
import model.Schedule;

public class FashionShowService {

    private final Schedule schedule;
    private final Events events;
    private final FinanceService finance;

    public FashionShowService(Schedule schedule, Events events, FinanceService finance) {
        this.schedule = schedule;
        this.events = events;
        this.finance = finance;
    }

    public void runFashionShow(List<String> products, int expectedAttendance,
                               String venue, double projectedProfit, LocalDate date) {

        Scanner scan = new Scanner(System.in);

        System.out.println("\n--- FASHION SHOW SCHEDULING STARTED ---");

        System.out.println("Products Added: " + products);

        schedule.generateSchedule(products);
        
        if (!schedule.approveSchedule()) {
            System.out.println("Schedule NOT approved. Cancelling Fashion Show.");
            return;
        }

        
        System.out.println("Expected Attendees: " + expectedAttendance);
        System.out.println("Venue: " + venue);

        events.addEvent(venue, date);

        System.out.print("Simulate show? (Y/N): ");
        String answer = scan.nextLine();

        if (answer.equalsIgnoreCase("Y")) {
            finance.addSale(projectedProfit);
            System.out.println("Fashion Show completed! Profit Added: " + projectedProfit);
        } else {
            System.out.println("Fashion Show completed without simulation.");
        }
    }
}
