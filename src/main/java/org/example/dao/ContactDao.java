package org.example.dao;

import org.example.model.Contact;

public interface ContactDao extends GenericDao<Contact,Long>{
    public Contact findByEmail(String email);
}
