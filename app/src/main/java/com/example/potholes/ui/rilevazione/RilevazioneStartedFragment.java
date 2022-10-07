package com.example.potholes.ui.rilevazione;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.potholes.R;
import com.example.potholes.communication.SocketClient;
import com.example.potholes.services.PosizioneService;
import com.example.potholes.services.RilevazioneService;

public class RilevazioneStartedFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rilevazione_started_fragment, container, false);
        SocketClient socketClient = new SocketClient(view.getContext());
        socketClient.startSogliaRequest();
        PosizioneService posizioneService = new PosizioneService(getContext());
        RilevazioneService rilevazioneService = new RilevazioneService(view.getContext(),posizioneService);
        rilevazioneService.startRilevazione();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Button interrompiButton= view.findViewById(R.id.rilevaButtonFragment);
        interrompiButton.setOnClickListener(v-> {
            rilevazioneService.stopRilevazione();
            fragmentTransaction.replace(R.id.fragmentContainerView2, RilevazioneFragment.class,null)
                    .setReorderingAllowed(true)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });

        return view;
    }


}