package com.example.bmi.database;

import com.google.android.gms.maps.model.LatLng;

public class UserProfile {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String bank;
    private String accountNo;
    private LatLng location;
    private String password;
    // Constructor
    public UserProfile() {
        // Default constructor
    }

    // Getters and setters for each attribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getBankName() {
        return bank;
    }

    public void setBankName(String banked) {
        this.bank = banked;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    // Constructor, getters, and setters...

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
