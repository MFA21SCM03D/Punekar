package com.example.punekar.Models;

public class Advertise {

    String address;
    String tenure;
    String roommateNeeded;
    String contact;
    String name;
    String totalTenants;
    String userID;
    String rent;

    public Advertise() {
    }

    public Advertise(String address, String tenure, String roommateNeeded, String contact, String name, String totalTenants, String userID, String rent) {
        this.address = address;
        this.tenure = tenure;
        this.roommateNeeded = roommateNeeded;
        this.contact = contact;
        this.name = name;
        this.totalTenants = totalTenants;
        this.userID = userID;
        this.rent = rent;
    }

    public String getAddress() {
        return address;
    }

    public String getTenure() {
        return tenure;
    }

    public String getRoommateNeeded() {
        return roommateNeeded;
    }

    public String getContact() {
        return contact;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTenure(String tenure) {
        this.tenure = tenure;
    }

    public void setRoommateNeeded(String roommateNeeded) {
        this.roommateNeeded = roommateNeeded;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalTenants() {
        return totalTenants;
    }

    public void setTotalTenants(String totalTenants) {
        this.totalTenants = totalTenants;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }
}
