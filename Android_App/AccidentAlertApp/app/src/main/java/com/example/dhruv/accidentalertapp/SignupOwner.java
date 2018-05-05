package com.example.dhruv.accidentalertapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupOwner extends AppCompatActivity {

    Button b_register;
    TextView b_userID;
    TextView b_name;
    TextView b_contactno;
    TextView b_emergencyno;
    TextView b_email;
    TextView b_password;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner);

        b_register = (Button) findViewById(R.id.register);
        b_email = (TextView) findViewById(R.id.email);
        b_password = (TextView) findViewById(R.id.pass);
        b_name = (TextView) findViewById(R.id.name);
        b_userID = (TextView) findViewById(R.id.userID);
        b_contactno = (TextView) findViewById(R.id.contactno);
        b_emergencyno = (TextView) findViewById(R.id.emergencyno);

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
        String contactno = b_contactno.getText().toString();
        String emergencyno = b_emergencyno.getText().toString();
        writeNewOwner(userID,name,emailID,password,contactno,emergencyno);

        Intent myIntent = new Intent(SignupOwner.this,
                ownerView.class);
        myIntent.putExtra("value",userID);
        startActivity(myIntent);
    }


    private void writeNewOwner(String userId, String name, String email,String password,String contactno , String emergencyno) {
        Vehicle user = new Vehicle(userId,name,email,password,contactno,emergencyno);
        myRef.child("vehicles").child(userId).setValue(user);
    }


}
