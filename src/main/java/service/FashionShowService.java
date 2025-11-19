package service;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import model.Events;
import model.FashionShow;
import model.Schedule;

public class FashionShowService {
    
    private final Schedule schedule;
    private final Events events;
    private final FinanceService financeService;

    public FashionShowService(Schedule schedule, Events events, FinanceService financeService){
        this.schedule = schedule;
        this.events = events;
        this.financeService = financeService;
    }

    public void runFashionShow(List<String> items, int expectedAttendance, String venue, double projectedProfit, LocalDate date){
        
        FashionShow show = new FashionShow(schedule, events, financeService);
        Scanner scan = new Scanner(System.in);
        show.scheduleFashionShow();
        show.inputItemsToShow(items);
        show.generateSchedule();
        if (!schedule.approveSchedule()) {
            System.out.println("Schedule not approved. Fashion Show cannot proceed.");
            return;
        }
        show.inputExpectedIntendees(expectedAttendance);
        show.inputVenue(venue);
        System.out.println("Simulate show? (Y/N)");
        String answer = scan.nextLine();
        if (answer.equalsIgnoreCase("Y")){
            show.addProfit(projectedProfit);
            show.addEvent(date);
            System.out.println("Fashion Show Successfuly Completed");
        }
        if (answer.equalsIgnoreCase("N")){
            show.addEvent(date);
            System.out.println("Fashion Show Successfuly Completed");
        }

    }
}
