package com.example.potholes.services;

import com.example.potholes.communication.SocketClient;
import com.example.potholes.entity.Evento;

import java.util.ArrayList;
import java.util.List;

public class EventiViciniService {
    private static EventiViciniService entity = null;
    private final PosizioneService posizioneService;
    private final SocketClient socketClient;
    private static List<Evento> eventoList;

    public List<Evento> getEventoList() {
        return eventoList;
    }

    public static EventiViciniService getInstance(PosizioneService posizioneService, SocketClient socketClient) {
        if(entity == null) {
            entity = new EventiViciniService(posizioneService,socketClient);
        }
        return entity;
    }

    private EventiViciniService(PosizioneService posizioneService, SocketClient socketClient) {
        if(eventoList == null) eventoList = new ArrayList<>();
        this.posizioneService = posizioneService;
        this.socketClient = socketClient;
    }

    private void eventiViciniRequest() {
        synchronized (posizioneService) {
            while (posizioneService.getPosizione() == null) {
                try {
                    posizioneService.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            socketClient.startEventiViciniRequest(posizioneService.getPosizione(),eventoList);
        }
    }

    public void startRequestEventiVicini() {
        new Thread(this::eventiViciniRequest).start();
    }
}
