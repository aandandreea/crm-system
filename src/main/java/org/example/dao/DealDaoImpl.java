package org.example.dao;

import org.example.exception.CustomException;
import org.example.model.Customer;
import org.example.model.Deal;
import org.example.model.DealStage;
import org.example.util.DatabasePool;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DealDaoImpl implements DealDao{
    @Override
    public void save(Deal deal) {
       try(Connection conn = DatabasePool.getConnection()){
           save(deal,conn);
        }catch(SQLException e){
           throw new CustomException("Couldn't save deal.",e);
        }
    }

    @Override
    public void save(Deal deal, Connection conn){
        String sql = "INSERT INTO deals (title, amount, stage, customer_id) VALUES (?, ?, ?, ?)";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,deal.getTitle());
            pstmt.setBigDecimal(2,deal.getAmount());
            pstmt.setString(3,deal.getStage().name());
            pstmt.setLong(4,deal.getCustomerId());

            pstmt.executeUpdate();
            System.out.println("Deal saved.");
        }catch(SQLException e){
            throw new CustomException("Couldn't save deal.",e);
        }
    }

    @Override
    public Deal findById(Long id) {
        String sql = "SELECT deal_id, title, amount, stage, customer_id FROM deals WHERE deal_id = ?";
        try(Connection conn = DatabasePool.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1,id);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    Deal deal = new Deal(rs.getLong("deal_id"),
                                        rs.getString("title"),
                                        rs.getBigDecimal("amount"),
                                        DealStage.valueOf(rs.getString("stage")),
                                        rs.getLong("customer_id"));
                    return deal;
                }
            }
        }catch(SQLException e){
            throw new CustomException("Deal not found." ,e);
        }
        return null;
    }

    @Override
    public List<Deal> findAll() {
        String sql = "SELECT * FROM deals";
        List<Deal> list = new ArrayList<>();
        try(Connection conn = DatabasePool.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                list.add(new Deal(rs.getLong("deal_id"),
                        rs.getString("title"),
                        rs.getBigDecimal("amount"),
                        DealStage.valueOf(rs.getString("stage")),
                        rs.getLong("customer_id")));
            }
            return list;
        } catch(SQLException e){
            throw new CustomException("List of deals not found.",e);
        }
    }

    @Override
    public void update(Deal deal) {
        String sql = "UPDATE deals SET title = ?, amount = ?, stage = ?, customer_id = ? WHERE deal_id = ?";
        try (Connection conn = DatabasePool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, deal.getTitle());
            pstmt.setBigDecimal(2, deal.getAmount());
            pstmt.setString(3, deal.getStage().name());
            pstmt.setLong(4, deal.getCustomerId());
            pstmt.setLong(5, deal.getDealId());

            pstmt.executeUpdate();
            System.out.println("Deal updated successfully.");
        } catch (SQLException e) {
            throw new CustomException("Deal update failed." ,e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM deals WHERE deal_id = ?";
        try (Connection conn = DatabasePool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            System.out.println("Deal deleted successfully.");
        } catch (SQLException e) {
            throw new CustomException("Couldn't delete deal.",e);
        }
    }
    @Override
    public List<Deal> findByCustomerId(Long customerId){
        String sql = "SELECT * FROM deals WHERE customer_id = ?";
        List<Deal> list = new ArrayList<>();
        try(Connection conn = DatabasePool.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1,customerId);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    list.add(new Deal(rs.getLong("deal_id"),
                                        rs.getString("title"),
                                        rs.getBigDecimal("amount"),
                                        DealStage.valueOf(rs.getString("stage")),
                                        rs.getLong("customer_id")));
                }
                return list;
            }
        }catch(SQLException e){
            throw new CustomException("Deals not found for this customer.",e);
        }
    }
}
