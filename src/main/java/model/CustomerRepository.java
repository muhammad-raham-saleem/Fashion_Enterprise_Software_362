package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing customer data persistence
 *
 * @author Sheldon Corkery
 */
public class CustomerRepository implements Repository<Customer> {

    private final String filePath;
    private final List<Customer> customers;

    public CustomerRepository(String filePath) {
        this.filePath = filePath;
        this.customers = new ArrayList<>();
        loadFromFile();
    }

    @Override
    public List<Customer> getAll() {
        return new ArrayList<>(customers);
    }

    @Override
    public Customer findById(int id) {
        return customers.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Customer findByName(String name) {
        return customers.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Find customer by email address
     */
    public Customer findByEmail(String email) {
        return customers.stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Add a new customer
     */
    public void add(Customer customer) {
        // Check if customer with same email already exists
        if (findByEmail(customer.getEmail()) != null) {
            System.out.println("Customer with email " + customer.getEmail() + " already exists");
            return;
        }
        customers.add(customer);
        saveToFile();
    }

    /**
     * Update existing customer
     */
    public void update(Customer customer) {
        Customer existing = findById(customer.getId());
        if (existing != null) {
            customers.remove(existing);
            customers.add(customer);
            saveToFile();
        }
    }

    /**
     * Get next available customer ID
     */
    public int getNextId() {
        return customers.stream()
                .mapToInt(Customer::getId)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public Customer parseLine(String line) {
        return Customer.fromCsv(line);
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

                Customer customer = parseLine(line);
                if (customer != null) {
                    customers.add(customer);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
    }

    private void saveToFile() {
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Write header
            writer.println("id,name,email,phone");

            // Write customer data
            for (Customer customer : customers) {
                writer.println(customer.toCsv());
            }
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }
}

