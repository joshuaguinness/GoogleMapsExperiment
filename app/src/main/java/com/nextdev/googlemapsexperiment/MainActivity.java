package com.nextdev.googlemapsexperiment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static java.util.logging.Logger.global;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    final int PERMISSION_LOCATION = 111;

    ArrayList<Event> events = new ArrayList<>();

    TextView outputDestinations;
    EditText typeDestination;
    TextView percentageOutput;

    double goalLatitude;
    double goalLongitude;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        outputDestinations = (TextView) (findViewById(R.id.outputDestinations));
        typeDestination = (EditText) (findViewById(R.id.typeDestination));
        percentageOutput = (TextView) (findViewById(R.id.percentageOutput));

        Button selectDestinationButton = (Button) (findViewById(R.id.selectDestinationButton));

        // Creates a new array list of object events

        events.add(new Event("Marauder Zone", "Saturday", "9:00 AM to 4:00 PM", "BSB", "43.262259", "-79.919985"));
        events.add(new Event("Faculty Swag Distribution", "Saturday", "10:00 AM to 4:00 PM", "ETB", "43.258226", "-79.920013"));
        events.add(new Event("SOCS Opening Ceremonies", "Saturday", "1:00 PM to 1:30 PM", "MDCL", "43.261335", "-79.916970"));
        events.add(new Event("Residence Dinner: McKay", "Saturday", "5:00 PM to 5:45 PM", "Thode", "43.261070", "-79.922472"));

        outputDestinations.setText("1. BSB, 2. ETB, 3. MDCL, 4. Thode");

        selectDestinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int destination = Integer.parseInt(typeDestination.getText().toString());

                if (destination == 1) {
                    goalLatitude = Double.parseDouble(events.get(0).getLatitude());
                    goalLongitude = Double.parseDouble(events.get(0).getLongitude());
                } else if (destination == 2) {
                    goalLatitude = Double.parseDouble(events.get(1).getLatitude());
                    goalLongitude = Double.parseDouble(events.get(1).getLongitude());
                } else if (destination == 3) {
                    goalLatitude = Double.parseDouble(events.get(2).getLatitude());
                    goalLongitude = Double.parseDouble(events.get(2).getLongitude());
                } else if (destination == 4) {
                    goalLatitude = Double.parseDouble(events.get(3).getLatitude());
                    goalLongitude = Double.parseDouble(events.get(3).getLongitude());
                }

                outputDestinations.setText("Lat: " + goalLatitude + ", Long: " + goalLongitude);
            }
        });


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
            Log.v("DONKEY", "Requesting Permissions");
        } else {
            Log.v("Donkey", "starting location services from onConnected");
            startLocationServices();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        percentageOutput.setText("Lat: " + latitude + ", Long: " + longitude);

        Log.v("WORK", "Lat:" + latitude + " - Long:" + longitude);

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationServices();
                    Log.v("DONKEY", "Permission Granted - Starting Services");
                } else {
                    //Show a dialog saying something like "I can't see your location"
                    Log.v("DONKEY", "Permission not granted");
                }
            }
        }
    }

    public void startLocationServices() {
        Log.v("DONKEY", "starting location services called");

        try {
            LocationRequest req = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, req, this);
            Log.v("DONKEY", "Requestion Location Updates");
        } catch (SecurityException exception) {
            //Should dialog to user
            Log.v("DONKEY", exception.toString());
        }


    }
}


