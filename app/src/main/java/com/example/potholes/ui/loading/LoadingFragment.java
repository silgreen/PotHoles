package com.example.potholes.ui.loading;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.example.potholes.R;
import com.example.potholes.communication.SocketClient;
import com.example.potholes.services.EventiViciniService;
import com.example.potholes.services.PosizioneService;

public class LoadingFragment extends Fragment {
    private Handler handler;
    private View container;
    private EventiViciniService eventiViciniService;
    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(false) {
        @Override
        public void handleOnBackPressed() {
            NavDirections navDirections = LoadingFragmentDirections.actionLoadingFragmentToNavigationHome();
            Navigation.findNavController(container).navigate(navDirections);
        }
    };
    private final Runnable changeFragment = new Runnable() {
        @Override
        public void run() {
            if(eventiViciniService.getEventoSet().isEmpty()) {
                Toast.makeText(getContext(), "Nessun evento vicino trovato", Toast.LENGTH_SHORT).show();
                NavDirections navDirections = LoadingFragmentDirections.actionLoadingFragmentToNavigationHome();
                Navigation.findNavController(container).navigate(navDirections);
            } else {
                NavDirections navDirections = LoadingFragmentDirections.actionLoadingFragmentToNavigationEventi();
                Navigation.findNavController(container).navigate(navDirections);
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading,container,false);
        this.container = container;
        startRequestEventiViciniOperations();
        createHandler();
        onBackPressedCallback.setEnabled(true);
        if(getActivity() != null)
            requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(),onBackPressedCallback);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(changeFragment);
        onBackPressedCallback.setEnabled(false);
    }

    public void startRequestEventiViciniOperations() {
        PosizioneService posizioneService = new PosizioneService(getContext());
        posizioneService.startLocation();
        SocketClient socketClient = new SocketClient(getContext());
        eventiViciniService = EventiViciniService.getInstance(posizioneService,socketClient);
        eventiViciniService.startRequestEventiVicini();
    }

    public void createHandler() {
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(changeFragment,5000);
    }
}