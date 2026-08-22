package org.example.dao;

import org.example.model.Deal;

import java.sql.Connection;
import java.util.List;

public interface DealDao extends GenericDao<Deal,Long>{
    List<Deal> findByCustomerId(Long customerId);
}
