package org.example.dao;

import org.example.exception.CustomException;
import org.example.model.Customer;
import org.example.util.DatabasePool;

import javax.xml.crypto.Data;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public void save(Customer customer) {
        try(Connection conn = DatabasePool.getConnection()){
            save(customer,conn);
        } catch (SQLException e) {
            throw new CustomException("Customer cannot be added." ,e);
        }
    }
    @Override
    public void save(Customer customer, Connection conn){
        String sql = "INSERT INTO customers (name, industry, primary_contact) VALUES (?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getIndustry());
            pstmt.setLong(3, customer.getPrimaryContact());

            pstmt.executeUpdate();
            System.out.println("Customer added successfully.");
        } catch (SQLException e) {
            throw new CustomException("Customer cannot be added." ,e);
        }
    }
    @Override
    public Customer findById(Long id) {
        String sql = "SELECT customer_id, name, industry, primary_contact FROM customers where customer_id = ?";

        try (Connection conn = DatabasePool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Customer customer = new Customer(rs.getLong("customer_id"),
                            rs.getString("name"),
                            rs.getString("industry"),
                            rs.getLong("primary_contact"));
                    System.out.println("Customer found");
                    return customer;
                }
            }
        } catch (SQLException e) {
            throw new CustomException("Customer not found" ,e);
        }
        return null;
    }
    @Override
    public List<Customer> findAll(){
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try(Connection conn = DatabasePool.getConnection(); Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while(rs.next()){
                list.add(new Customer(rs.getLong("customer_id"),
                        rs.getString("name"),
                        rs.getString("industry"),
                        rs.getLong("primary_contact")));
            }
            return list;
        } catch(SQLException e){
            throw new CustomException("Customers not found.",e);
        }
    }
    @Override
    public void update(Customer customer){
        String sql = "UPDATE customers SET name = ? ,industry = ?, primary_contact = ? WHERE customer_id = ?";
        try(Connection conn = DatabasePool.getConnection();
            PreparedStatement ptsmt = conn.prepareStatement(sql)){
                ptsmt.setString(1,customer.getName());
                ptsmt.setString(2, customer.getIndustry());
                ptsmt.setLong(3,customer.getPrimaryContact());
                ptsmt.setLong(4,customer.getCustomerId());
                ptsmt.executeUpdate();
            System.out.println("Customer updated successfully.");
        } catch(SQLException e){
            throw new CustomException("Customer update failed." ,e);
        }
    }
@Override
    public void delete(Long id){
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try(Connection conn = DatabasePool.getConnection();
            PreparedStatement ptsmt = conn.prepareStatement(sql )){
            ptsmt.setLong(1,id);
            ptsmt.executeUpdate();
        } catch(SQLException e){
            throw new CustomException("The customer delete has failed.",e);
        }
    }


}
