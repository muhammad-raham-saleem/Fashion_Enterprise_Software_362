package model;

import java.util.Objects;

/**
 * Represents a customer in the fashion system.
 * Customers can be extracted from receipts and online orders.
 *
 * @author Sheldon Corkery
 */
public class Customer {
    private final int id;
    private String name;
    private String email;
    private String phone;

    public Customer(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Validates email format (basic validation)
     */
    public boolean hasValidEmail() {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Customer{id=" + id + ", name='" + name + "', email='" + email + "', phone='" + phone + "'}";
    }

    /**
     * Serialize customer to CSV format
     */
    public String toCsv() {
        return id + "," + name + "," + email + "," + (phone != null ? phone : "");
    }

    /**
     * Parse customer from CSV line
     */
    public static Customer fromCsv(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        if (parts.length < 3) {
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String email = parts[2].trim();
            String phone = parts.length > 3 && !parts[3].trim().isEmpty() ? parts[3].trim() : null;
            return new Customer(id, name, email, phone);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

