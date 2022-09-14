package com.example.potholes.communication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.potholes.RilevazioneEventiAdapter;
import com.example.potholes.entity.Evento;
import com.example.potholes.ui.rilevazione.RilevazioneViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketClient {
    private final Context context;
    private final String LISTA = "lista";
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private List<Evento> eventoList;

    public SocketClient(Context context) {
        this.context = context;
        eventoList = new ArrayList<>();
    }

    private final Runnable sogliaThread = () -> {
        initSocket();
        sendUsername();
        if(checkResponseOK()) {
            richiestaSoglia();
            leggiSoglia();
        }
        socket = null;
    };

    private void initSocket() {
        if(socket == null) {
            try {
                InetAddress serverAddress = InetAddress.getByName("172.21.3.27");
                socket = new Socket(serverAddress,8080);
                writer = new PrintWriter(socket.getOutputStream(),true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Evento> getEventoList() {
        return eventoList;
    }

    public String getUsernameFromPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("info",Context.MODE_PRIVATE);
        return sharedPreferences.getString("username","UTENTE");
    }

    private void sendUsername() {
        String username = getUsernameFromPreferences();
        writer.println(username);
    }

    private boolean checkResponseOK() {
        String response = "";
        try {
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.equals("ok");
    }

    private void richiestaSoglia() {
        final String SOGLIA = "soglia";
        writer.println(SOGLIA);
    }

    private void leggiSoglia() {
        String soglia = "";
        try {
            soglia = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        salvaSoglia(soglia);
    }

    private void salvaSoglia(String soglia) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("info",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("soglia",soglia);
        editor.apply();
    }

    public void startSogliaRequest() {
        new Thread(sogliaThread).start();
    }

    public void richiestaEvento() {
        String EVENTO = "evento";
        writer.println(EVENTO);
    }

    public void startEventoRequest(Evento evento) {
        Runnable eventoThread = () -> {
            initSocket();
            sendUsername();
            if(checkResponseOK()) {
                richiestaEvento();
                if(checkResponseOK()) {
                    writer.println(getUsernameFromPreferences() + ";" + evento.toString());
                    try {
                        String tipoEvento = reader.readLine();
                        Log.d("tipo evento",tipoEvento);
                        evento.setTipoEvento(tipoEvento);
                        Evento.EventoListClass.getEventoList().add(evento);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socket = null;
                }
            }
        };
        new Thread(eventoThread).start();
    }
}
