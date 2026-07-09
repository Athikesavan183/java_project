package com.example.customerapp;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Console entry point: collects customer details from the user and
 * saves them to the database via CustomerDAO / proc_insertcustomer.
 */
public class Main {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomerDAO customerDAO = new CustomerDAO();

        System.out.println("=== New Customer Registration ===");

        String fullName = promptRequired(scanner, "Full Name: ");
        String phone = promptRequired(scanner, "Phone: ");
        String email = promptEmail(scanner, "Email: ");
        String address = promptRequired(scanner, "Address: ");
        String city = promptRequired(scanner, "City: ");
       

        Customer customer = new Customer(
                fullName, phone, email, address, city);

        System.out.println("\nSaving customer...");
        try {
            customerDAO.insertCustomer(customer);
            System.out.println("Customer saved successfully: " + customer);
        } catch (SQLException e) {
            System.err.println("Failed to save customer to the database.");
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static String promptRequired(Scanner scanner, String label) {
        String value;
        while (true) {
            System.out.print(label);
            value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field cannot be empty. Please try again.");
        }
    }

    private static String promptEmail(Scanner scanner, String label) {
        String value;
        while (true) {
            value = promptRequired(scanner, label);
            if (EMAIL_PATTERN.matcher(value).matches()) {
                return value;
            }
            System.out.println("Invalid email format. Please try again.");
        }
    }
}
