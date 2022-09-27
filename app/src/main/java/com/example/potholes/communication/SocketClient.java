package com.example.potholes.communication;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import com.example.potholes.RilevazioneEventiAdapter;
import com.example.potholes.entity.Evento;
import com.example.potholes.services.PosizioneService;
import com.example.potholes.ui.eventi.EventiAdapter;
import com.example.potholes.ui.rilevazione.RilevazioneViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SocketClient {
    private final Context context;
    private final String LISTA = "lista";
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;

    public SocketClient(Context context) {
        this.context = context;
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
                InetAddress serverAddress = InetAddress.getByName("172.18.63.113");
                socket = new Socket(serverAddress,8080);
                writer = new PrintWriter(socket.getOutputStream(),true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public void richiestaLista() {
        String LISTA = "lista";
        writer.println(LISTA);
    }

    public List<Evento> deserializeEvento() {
        String s = "";
        List<Evento> eventiViciniList = new ArrayList<>();
        while (s != null) {
            try {
                s = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(s!= null) {
                String[] arr = s.split(";", 4);
                Evento evento = new Evento(Double.parseDouble(arr[2]), Double.parseDouble(arr[3]), arr[1]);
                eventiViciniList.add(evento);
                Log.d("evento dopo lo split", evento.toString());
            }
        }
        return eventiViciniList;
    }

    public void startEventiViciniRequest(Location location,List<Evento> eventoList) {
        Runnable eventiViciniThread = () -> {
            initSocket();
            sendUsername();
            if(checkResponseOK()) {
                richiestaLista();
                if(checkResponseOK()) {
                    writer.println(getUsernameFromPreferences() + ";" + location.getLatitude() + ";" + location.getLongitude());
                    eventoList.addAll(deserializeEvento());
                    Log.d("lista eventi vicini",eventoList.toString());
                }
            }
            socket = null;
        };
        new Thread(eventiViciniThread).start();
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
                        Evento.EventoListClass.getEventoListRilevazione().add(evento);
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
