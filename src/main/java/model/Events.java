package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import util.FileManager;

public class Events {

    private List<String> events = new ArrayList<>();
    private final String filename = "data/events.txt";

    public Events(){
        loadEvents();
    }

    public void addEvent(String venue, LocalDate date) {
        String eventData = "Fashion show, " + venue + ", " +  date;
        events.add(eventData);

        System.out.println("Event Added: " + eventData);

        saveEvent();
    }

    public void loadEvents(){
        events.clear();
        events.addAll(FileManager.readLines(filename));
    }

    public void saveEvent(){
        FileManager.writeLines(filename, events);
    }
}
