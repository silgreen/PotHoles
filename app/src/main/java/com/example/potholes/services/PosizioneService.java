package com.example.potholes.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class PosizioneService {
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest ;
    private LocationCallback locationCallback;
    private volatile Location posizione;
    private double latitudine;
    private double longitudine;

    public void setPosizione(Location posizione) {
        this.posizione = posizione;
    }

    public Location getPosizione() {
        return posizione;
    }

    public PosizioneService(Context context){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        instantiateLocationRequest();
        instantiateLocationCallback();
    }

    public double getLatitudine() {
        return latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void instantiateLocationCallback(){
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                posizione = locationResult.getLastLocation();
            }
        };
    }

    public void instantiateLocationRequest(){
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(500);
        locationRequest.setInterval(1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
    }

    @SuppressLint("MissingPermission")
    public void startLocation(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
    }

    public void stopLocation(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @SuppressLint("MissingPermission")
    public void retrieveLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                synchronized (PosizioneService.this) {
                    if(task.isSuccessful() && task.getResult() != null) {
                        posizione = task.getResult();
                        latitudine = posizione.getLatitude();
                        longitudine = posizione.getLongitude();
                        PosizioneService.this.notifyAll();
                        Log.d("latitudine",latitudine + "");
                        Log.d("longitudine",longitudine + "");
                    }
                }
            }
        });
    }
}