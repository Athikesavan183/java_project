package com.example.customerapp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Data access class responsible for persisting a Customer by calling
 * the proc_insertcustomer PostgreSQL stored procedure.
 */
public class CustomerDAO {

    // CALL syntax with 7 placeholders matching proc_insertcustomer's parameters
    private static final String CALL_INSERT_CUSTOMER =
            "call proc_insertcustomer(?, ?, ?, ?, ?)";

    /**
     * Inserts a customer record by invoking proc_insertcustomer.
     *
     * @param customer the customer details collected from the user
     * @throws SQLException if the database call fails
     */
    public void insertCustomer(Customer customer) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(CALL_INSERT_CUSTOMER)) {

            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getAddress());
            stmt.setString(5, customer.getCity());
            

          
            stmt.execute();
        }
    }
}
