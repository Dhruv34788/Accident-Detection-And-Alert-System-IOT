package com.example.dhruv.accidentalertapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button b_login;
    Button b_signup;
    TextView b_password;
    TextView b_userID;
    RadioButton radioButton;
    RadioGroup radioGroup;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRef1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b_login = (Button) findViewById(R.id.login);
        b_signup = (Button) findViewById(R.id.signup);
        b_password = (TextView) findViewById(R.id.pass);
        b_userID = (TextView) findViewById(R.id.userID);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);

        b_signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                if(selectedId==-1){
                    Toast.makeText(Login.this,"Nothing selected", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(radioButton.getText().equals("volunteer")){
                        Intent myIntent = new Intent(Login.this,
                                SignupVolunteer.class);
                        startActivity(myIntent);
                    }
                    else if (radioButton.getText().equals("vehicle owner")){
                        Intent myIntent = new Intent(Login.this,
                                SignupOwner.class);
                        startActivity(myIntent);
                    }
                }
            }
        });

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                if(selectedId==-1){
                    Toast.makeText(Login.this,"Nothing selected", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(radioButton.getText().equals("volunteer")){
                        login("users");
                    }
                    else if (radioButton.getText().equals("vehicle owner")){
                        login("vehicles");
                    }
                }
            }
        });

    }


    private void login(final String node){
        final String password = b_password.getText().toString();
        final String userID = b_userID.getText().toString();

        // Read from the database
        myRef.child(node).child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.child("password").getValue().toString();
                if(value.equals(password)) {
                    Toast.makeText(Login.this,"Login Successful", Toast.LENGTH_SHORT).show();
                    if(node.equals("users")){
                        Intent myIntent = new Intent(Login.this,
                                volunteerView.class);
                        myIntent.putExtra("value",userID);
                        startActivity(myIntent);
                    }
                    else if (node.equals("vehicles")){
                        Intent myIntent = new Intent(Login.this,
                                ownerView.class);
                        myIntent.putExtra("value",userID);
                        startActivity(myIntent);
                    }
                }
                else{
                    Toast.makeText(Login.this,"Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("aa", "Failed to read value.", error.toException());
            }
        });


    }
}
