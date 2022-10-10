package com.example.potholes.ui.eventi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.potholes.R;
import com.example.potholes.communication.SocketClient;
import com.example.potholes.entity.Evento;
import com.example.potholes.services.EventiViciniService;
import com.example.potholes.services.PosizioneService;

import java.util.Set;

public class EventiFragment extends Fragment {
    private PosizioneService posizioneService;
    private SocketClient socketClient;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        posizioneService = new PosizioneService(getContext());
        posizioneService.startLocation();
        socketClient = new SocketClient(getContext());
        View view = inflater.inflate(R.layout.eventi_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewEventi);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        EventiViciniService eventiViciniService = EventiViciniService.getInstance(posizioneService, socketClient);
        EventiAdapter eventiAdapter = new EventiAdapter(eventiViciniService.getEventoList());
        recyclerView.setAdapter(eventiAdapter);
        if(eventiViciniService.getEventoList().isEmpty()) {
            NavDirections navDirections = EventiFragmentDirections.actionNavigationEventiToLoadingFragment();
            if(container != null)
                Navigation.findNavController(container).navigate(navDirections);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Set<Evento> eventoList = EventiViciniService.getInstance(posizioneService,socketClient).getEventoList();
        if(!eventoList.isEmpty()) eventoList.clear();
    }
}