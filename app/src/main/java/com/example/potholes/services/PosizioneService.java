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

import javax.xml.transform.Result;

public class PosizioneService {
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest ;
    private LocationCallback locationCallback;
    private Location posizione;
    private double latitudine;
    private double longitudine;

    @SuppressLint("MissingPermission")
    public Location getPosizione() {
        return posizione;
    }

    public PosizioneService(Context context){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        instantiateLocationRequest();
        instantiateLocationCallback();
    }

    public void instantiateLocationCallback(){
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                synchronized (PosizioneService.this) {
                    posizione = locationResult.getLastLocation();
                    PosizioneService.this.notifyAll();
                    }
                }
        };
    }

    public void instantiateLocationRequest(){
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(50);
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

    public void setPositionToNull() {
        posizione = null;
    }
}