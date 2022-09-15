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

    public Evento(double lat,double lng, String tipoEvento) {
        location = new Location("locationProvider");
        location.setLatitude(lat);
        location.setLongitude(lng);
        this.tipoEvento = tipoEvento;
        this.valore = 0;
    }

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
        private static List<Evento> eventoListRilevazione;
        private static List<Evento> eventoListEventiVicini;

        private static void initEventoListEventiVicini() {
            eventoListEventiVicini = new ArrayList<>();
        }

        public static List<Evento> getEventoListEventiVicini() {
            if(eventoListEventiVicini == null) initEventoListEventiVicini();
            return eventoListEventiVicini;
        }

        private static void initEventoListRilevazione() {
            eventoListRilevazione = new ArrayList<>();
        }

        public static List<Evento> getEventoListRilevazione() {
            if(eventoListRilevazione == null) initEventoListRilevazione();
            return eventoListRilevazione;
        }

    }
}
