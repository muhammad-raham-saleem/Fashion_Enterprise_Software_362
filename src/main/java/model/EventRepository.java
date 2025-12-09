package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository for managing event data persistence
 *
 * @author Sheldon Corkery
 */
public class EventRepository implements Repository<Event> {

    private final String filePath;
    private final List<Event> events;

    public EventRepository(String filePath) {
        this.filePath = filePath;
        this.events = new ArrayList<>();
        loadFromFile();
    }

    @Override
    public List<Event> getAll() {
        return new ArrayList<>(events);
    }

    @Override
    public Event findById(int id) {
        return events.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Event findByName(String name) {
        return events.stream()
                .filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all approved events
     */
    public List<Event> getApprovedEvents() {
        return events.stream()
                .filter(Event::isFinanciallyApproved)
                .collect(Collectors.toList());
    }

    /**
     * Get all events pending financial approval
     */
    public List<Event> getPendingApprovalEvents() {
        return events.stream()
                .filter(e -> e.getStatus() == Event.EventStatus.PENDING_APPROVAL)
                .collect(Collectors.toList());
    }

    /**
     * Add a new event
     */
    public void add(Event event) {
        events.add(event);
        saveToFile();
    }

    /**
     * Update existing event
     */
    public void update(Event event) {
        Event existing = findById(event.getId());
        if (existing != null) {
            events.remove(existing);
            events.add(event);
            saveToFile();
        }
    }

    /**
     * Remove an event
     */
    public void remove(Event event) {
        events.remove(event);
        saveToFile();
    }

    /**
     * Get next available event ID
     */
    public int getNextId() {
        return events.stream()
                .mapToInt(Event::getId)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public Event parseLine(String line) {
        return Event.fromCsv(line);
    }

    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                // Skip header
                if (firstLine && line.startsWith("id,")) {
                    firstLine = false;
                    continue;
                }

                Event event = parseLine(line);
                if (event != null) {
                    events.add(event);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading events: " + e.getMessage());
        }
    }

    private void saveToFile() {
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Write header
            writer.println("id,name,venue,date,cost,capacity,status,rsvpCount,staffIds,rejectionNotes");

            // Write event data
            for (Event event : events) {
                writer.println(event.toCsv());
            }
        } catch (IOException e) {
            System.err.println("Error saving events: " + e.getMessage());
        }
    }
}

