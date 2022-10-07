package com.example.potholes.entity;

import android.location.Location;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

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
    public String toStringForSocket() {
        return location.getLatitude() + ";" + location.getLongitude() + ";" + valore;
    }

    @NonNull
    @Override
    public String toString() {
        return location.getLatitude() + ";" + location.getLongitude() + ";" + tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public static class EventoListClass {
        private static List<Evento> eventoListRilevazione;

        private static void initEventoListRilevazione() {
            eventoListRilevazione = new ArrayList<>();
        }

        public static List<Evento> getEventoListRilevazione() {
            if(eventoListRilevazione == null) initEventoListRilevazione();
            return eventoListRilevazione;
        }
    }
}
