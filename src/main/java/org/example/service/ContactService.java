package org.example.service;

import org.example.dao.ContactDao;
import org.example.dao.ContactDaoImpl;
import org.example.exception.CustomException;
import org.example.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactService {
    private final ContactDao contactDao = new ContactDaoImpl();

    public void createContact(String name, String email, String phoneNumber){
        if(name == null || name.isBlank() || name.matches(".*[-0-9@!#$%&,.';].*")){
            throw new CustomException("Invalid name");
        }
        if(email == null || email.isBlank() || !email.matches("^[A-Za-z0-9._+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$")){
            throw new CustomException("Invalid email.");
        }
        if(phoneNumber == null || phoneNumber.isBlank() || !phoneNumber.matches("\\d{10}")){
            throw new CustomException("Invalid phone number.");
        }
        if(contactDao.findByEmail(email) != null){
            throw new CustomException("Email already exists.");
        }

        contactDao.save(new Contact(name,email,phoneNumber));
    }
}
