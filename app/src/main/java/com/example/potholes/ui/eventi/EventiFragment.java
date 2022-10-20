package com.example.potholes.ui.eventi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.potholes.R;
import com.example.potholes.communication.SocketClient;
import com.example.potholes.services.EventiViciniService;
import com.example.potholes.services.PosizioneService;

public class EventiFragment extends Fragment {
    private PosizioneService posizioneService;
    private SocketClient socketClient;
    private View container;
    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(false) {
        @Override
        public void handleOnBackPressed() {
            NavDirections navDirections = EventiFragmentDirections.actionNavigationEventiToNavigationHome();
            Navigation.findNavController(container).navigate(navDirections);
        }
    };
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        posizioneService = new PosizioneService(getContext());
        posizioneService.startLocation();
        socketClient = new SocketClient(getContext());
        View view = inflater.inflate(R.layout.eventi_fragment, container, false);
        this.container = container;
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewEventi);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        EventiViciniService eventiViciniService = EventiViciniService.getInstance(posizioneService, socketClient);
        EventiAdapter eventiAdapter = new EventiAdapter(eventiViciniService.getEventoSet());
        recyclerView.setAdapter(eventiAdapter);
        if(eventiViciniService.getEventoSet().isEmpty()) {
            NavDirections navDirections = EventiFragmentDirections.actionNavigationEventiToLoadingFragment();
            if(container != null)
                Navigation.findNavController(container).navigate(navDirections);
        }
        onBackPressedCallback.setEnabled(true);
        if(getActivity() != null)
            requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(),onBackPressedCallback);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        EventiViciniService.getInstance(posizioneService,socketClient).getEventoSet().clear();
        onBackPressedCallback.setEnabled(false);
    }
}