package com.example.potholes.entity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Evento {
    private final Location location;
    private final double valore;
    private String tipoEvento;

    public Evento(Location location, double valore) {
        this.location = location;
        this.valore = valore;
    }

    public Location getLocation() {
        return location;
    }

    @NonNull
    @Override
    public String toString() {
        return location.getLatitude() + ";" + location.getLongitude() + ";" + valore;
    }

    public String eventoAddress(Geocoder geocoder) throws IOException {
        Address address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1).get(0);
        String provincia = address.getAdminArea();
        String city = address.getLocality();
        String stato = address.getCountryName();
        String via = address.getFeatureName();
        return  via + " " + city + " " + provincia + " " + stato + ":" + tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public static class EventoListClass {
        private static List<Evento> eventoList;

        private static void initEventoList() {
            eventoList = new ArrayList<>();
        }

        public static List<Evento> getEventoList() {
            if(eventoList == null) initEventoList();
            return eventoList;
        }

        public Evento getEventoFromList(int index) {
            return eventoList.get(index);
        }
    }
}
