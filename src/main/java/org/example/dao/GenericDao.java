package org.example.dao;

import java.sql.Connection;
import java.util.List;

public interface GenericDao<T,ID> {
    // T = tipul entitatii(Customer,Contact,Deal)
    // ID = tipul cheii primare

    void save(T entity);
    void save(T entity, Connection conn);
    T findById(ID id);
    List<T> findAll();
    void update(T entity);
    void delete(ID id);
}
