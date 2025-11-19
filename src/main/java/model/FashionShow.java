package model;

import java.time.LocalDate;
import java.util.List;

import service.FinanceService;

public class FashionShow {

    private List<String> items;
    private int expectedAttendees;
    private String venue;

    private Schedule schedule;
    private Events events;
    private FinanceService finance;

    public FashionShow(Schedule schedule, Events events, FinanceService finance) {
        this.schedule = schedule;
        this.events = events;
        this.finance = finance;
    }

    public void scheduleFashionShow(){
        System.out.println("Fashion Show Scheduling started");
    }

    public void inputItemsToShow(List<String> items){
    this.items = items; 
    System.out.println("Items Added:" + items);
    }


    public void addEvent(LocalDate date){
        events.addEvent(this.venue, date);
        System.out.println("Event Recorded");
    }

    public void generateSchedule(){
        schedule.generateSchedule(items);
    }

    public void inputExpectedIntendees(int num){
        this.expectedAttendees = num;
        System.out.println("Expected Attendees");
    }

    public void inputVenue(String venue){
        this.venue = venue;
        System.out.println("Venue is: " + venue);
    }

    public void addProfit(double amount){
        finance.addSale(amount);
    }
    
}
