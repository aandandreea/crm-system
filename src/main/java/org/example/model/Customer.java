package org.example.model;

public class Customer {
    private long customerId;
    private String name;
    private String industry;
    private long primaryContact;

    public Customer() {
    }

    public Customer(String name, String industry, long primaryContact) {
        this.name = name;
        this.industry = industry;
        this.primaryContact = primaryContact;
    }

    public Customer(long customerId, String name, String industry, long primaryContact) {
        this.customerId = customerId;
        this.name = name;
        this.industry = industry;
        this.primaryContact = primaryContact;
    }


    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public long getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(long primaryContact) {
        this.primaryContact = primaryContact;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", industry='" + industry + '\'' +
                ", primaryContact=" + primaryContact +
                '}';
    }
}
