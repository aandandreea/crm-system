package org.example.dao;

import org.example.exception.CustomException;
import org.example.model.Contact;
import org.example.util.DatabasePool;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDaoImpl implements ContactDao{

    @Override
    public void save(Contact contact){
        try(Connection conn = DatabasePool.getConnection()){
            save(contact,conn);
        }catch(SQLException e){
            System.out.println("Couldn't add contact." + e.getMessage());
        }
    }

    @Override
    public void save(Contact contact, Connection conn) {
        String sql = "INSERT INTO contacts (name, email, phone_number) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, contact.getName());
            pstmt.setString(2, contact.getEmail());
            pstmt.setString(3, contact.getPhoneNumber());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long generatedId = rs.getLong(1);
                    contact.setContactId(generatedId);
                }
            }
        }catch(SQLException e){
            throw new CustomException("Contact saving failed.",e);
        }
    }

    @Override
    public Contact findById(Long id) {
        String sql = "SELECT contact_id, name, email, phone_number FROM contacts WHERE contact_id = ?";
        try(Connection conn = DatabasePool.getConnection();
            PreparedStatement ptsmt = conn.prepareStatement(sql)){
            ptsmt.setLong(1,id);
            try(ResultSet rs = ptsmt.executeQuery()){
                while(rs.next()){
                    Contact contact = new Contact(rs.getLong("contact_id"),
                                                rs.getString("name"),
                                                rs.getString("email"),
                                                rs.getString("phone_number"));
                    return contact;
                }
            }
        } catch(SQLException e){
            throw new CustomException("Contact not found.",e);
        }
        return null;
    }

    @Override
    public List<Contact> findAll() {
        String sql = "SELECT * FROM contacts";
        List<Contact> list = new ArrayList<>();

        try(Connection conn = DatabasePool.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while(rs.next()){
                list.add(new Contact(rs.getLong("contact_id"),
                                    rs.getString("name"),
                                    rs.getString("email"),
                                    rs.getString("phone_number")));
            }
            return list;
        }catch(SQLException e){
            throw new CustomException("List of contacts not found.",e);
        }

    }

    @Override
    public void update(Contact contact) {
        String sql = "UPDATE contacts SET name = ?, email = ?, phone_number = ? WHERE contact_id = ?";
        try(Connection conn = DatabasePool.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, contact.getName());
            pstmt.setString(2, contact.getEmail());
            pstmt.setString(3, contact.getPhoneNumber());
            pstmt.setLong(4,contact.getContactId());

            pstmt.executeUpdate();
            System.out.println("Contact updated successfully.");
        }catch(SQLException e){
            throw new CustomException("Contact update failed.",e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM contacts WHERE contact_id = ?";
        try(Connection conn = DatabasePool.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1,id);
            pstmt.executeUpdate();
            System.out.println("Contact successfully deleted.");
        } catch(SQLException e){
            throw new CustomException("Couldn't delete the contact.",e);
        }
    }
    @Override
    public Contact findByEmail(String email){
        String sql = "SELECT * FROM contacts WHERE email = ?";

        try(Connection conn = DatabasePool.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,email);
            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Contact contact = new Contact(rs.getLong("contact_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone_number"));
                    return contact;
                }
            }
        }catch(SQLException e){
            throw new CustomException("Error searching contact by email.");
        }
        return null;
    }

}
