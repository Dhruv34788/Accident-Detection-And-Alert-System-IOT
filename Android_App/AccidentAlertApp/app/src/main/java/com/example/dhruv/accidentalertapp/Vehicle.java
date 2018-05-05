package com.example.dhruv.accidentalertapp;

public class Vehicle {
    public String userId;
    public String username;
    public String email;
    public String password;
    public String contactno;
    public String emergencyno;
    public  double latitude;
    public double longitude;

    public Vehicle() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Vehicle(String userId,String username, String email , String password, String contactno,String emergencyno) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.contactno = contactno;
        this.emergencyno = emergencyno;
    }
}
