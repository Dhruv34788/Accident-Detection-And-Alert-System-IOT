package com.example.dhruv.accidentalertapp;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignupVolunteer extends AppCompatActivity {

    Button b_register;
    TextView b_userID;
    TextView b_name;
    TextView b_email;
    TextView b_password;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_volunteer);

        b_register = (Button) findViewById(R.id.register);
        b_email = (TextView) findViewById(R.id.email);
        b_password = (TextView) findViewById(R.id.pass);
        b_name = (TextView) findViewById(R.id.name);
        b_userID = (TextView) findViewById(R.id.userID);

        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }

    private void signup(){
        String emailID =  b_email.getText().toString();
        String password = b_password.getText().toString();
        String name = b_name.getText().toString();
        String userID = b_userID.getText().toString();
        writeNewVolunteer(userID,name,emailID,password);

        Intent myIntent = new Intent(SignupVolunteer.this,
                volunteerView.class);
        myIntent.putExtra("value",userID);
        startActivity(myIntent);
    }


    private void writeNewVolunteer(String userId, String name, String email,String password) {
        Volunteer user = new Volunteer(userId,name, email,password);
        myRef.child("users").child(userId).setValue(user);
    }

}
