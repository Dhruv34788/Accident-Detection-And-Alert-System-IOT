package com.example.dhruv.accidentalertapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class volunteerView extends AppCompatActivity implements LocationListener ,OnMapReadyCallback {

    ToggleButton button;
    TextView text;
    Button save;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    LocationManager locationManager;
    DatabaseReference myRef1;
    private GoogleMap mMap;
    Marker markermy;
    Marker markervehicle;
    String vehicle;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_view);
        button = (ToggleButton) findViewById(R.id.button);
        text = (TextView) findViewById(R.id.text);
        save = (Button) findViewById(R.id.b_save);
        final String userID = getIntent().getExtras().getString("value");
        myRef1 = myRef.child("users").child(userID);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);
        getLocation();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),
                        "Good-job", Toast.LENGTH_SHORT).show();
                markervehicle.remove();
                text.setText("");
                myRef.child("accidents").child(vehicle).getRef().removeValue();
                myRef1.child("vehicleID").setValue("xx");
                button.performClick();
            }
        });
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myRef1.child("status").setValue(1);
                    Toast.makeText(getApplicationContext(),
                            "On-duty", Toast.LENGTH_SHORT).show();
                }
                else{
                    myRef1.child("status").setValue(0);
                    Toast.makeText(getApplicationContext(),
                            "Off-duty", Toast.LENGTH_SHORT).show();
                }
            }
        });


            // Read from the database
        myRef1.child("vehicleID").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String vehicleID = dataSnapshot.getValue().toString();

                    if (!(vehicleID.equals("xx"))){

                        myRef.child("vehicles").child(vehicleID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String ownerno = dataSnapshot.child("contactno").getValue().toString();
                                String backupno = dataSnapshot.child("emergencyno").getValue().toString();
                                String name = dataSnapshot.child("username").getValue().toString();
                                text.setText("");
                                text.append("vehicle no : "+vehicleID);
                                text.append("\nowner name : "+name);
                                text.append("\n"+"owner no : "+ownerno);
                                text.append("\n"+"emrgency no : "+backupno);
                                showVehicleLocation(mMap,vehicleID);
                                save.setVisibility(View.VISIBLE);
                                vehicle = vehicleID;
                                button.performClick();
                                }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if(markermy != null)
                        markermy.remove();
                    double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                    double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                    LatLng pos = new LatLng(latitude, longitude);
                    markermy = mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 18));

                } else {
                    Log.e("tag", "onDataChange: No data");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void showVehicleLocation(GoogleMap googleMap,String vehicleID) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        myRef.child("vehicles").child(vehicleID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if(markervehicle!= null)
                        markervehicle.remove();
                    double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                    double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                    String name = dataSnapshot.child("username").getValue().toString();
                    LatLng pos = new LatLng(latitude, longitude);
                    Log.e("tag", "dataxxxx");
                    markervehicle = mMap.addMarker(new MarkerOptions().position(pos).title(name));
                } else {
                    Log.e("tag", "onDataChange: No data");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void writeLocation(double latitude, double longitude) {
        myRef1.child("latitude").setValue(latitude);
        myRef1.child("longitude").setValue(longitude);
        Log.d("aaa", "sdasd");

    }


    @Override
    public void onLocationChanged(Location location) {
        writeLocation(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

