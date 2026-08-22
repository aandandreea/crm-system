package org.example.service;

import org.example.dao.ContactDao;
import org.example.dao.ContactDaoImpl;
import org.example.dao.CustomerDao;
import org.example.dao.CustomerDaoImpl;
import org.example.exception.CustomException;
import org.example.model.Contact;
import org.example.model.Customer;
import org.example.util.DatabasePool;

import java.sql.Connection;
import java.sql.SQLException;

public class CustomerService {
    private final ContactDao contactDao = new ContactDaoImpl();
    private final CustomerDao customerDao = new CustomerDaoImpl();

    public void createCustomerWithContact(Contact contact, String customerName, String industry) {
        Connection conn = null;
        try {
            conn = DatabasePool.getConnection();
            conn.setAutoCommit(false);

            contactDao.save(contact, conn);
            Customer customer = new Customer(customerName, industry, contact.getContactId());
            customerDao.save(customer, conn);

            conn.commit();
            System.out.println("Customer and contact created successfully.");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new CustomException("Rollback failed." ,rollbackEx);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    throw new CustomException("Failed to close connection.",e);
                }
            }
        }
    }
}
