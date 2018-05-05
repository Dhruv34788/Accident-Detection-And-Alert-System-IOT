package com.example.dhruv.accidentalertapp;

public class Volunteer {
    public String userId;
    public String username;
    public String email;
    public String password;
    public double latitude;
    public double longitude;
    public String vehicleID;
    public int status;
    public Volunteer() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Volunteer(String userId,String username, String email , String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.status = 0 ;
        this.latitude = 50;
        this.longitude = 50;
        this.vehicleID = "xx" ;
    }
}
