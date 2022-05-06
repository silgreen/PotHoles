package com.example.potholes.ui.rilevazione;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.potholes.R;
import com.example.potholes.services.RilevazioneService;

public class RilevazioneStartedFragment extends Fragment {

    private RilevazioneStartedViewModel mViewModel;

    public static RilevazioneStartedFragment newInstance() {
        return new RilevazioneStartedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rilevazione_started_fragment, container, false);
        RilevazioneService rilevazioneService = new RilevazioneService(view.getContext());
        rilevazioneService.startRilevazione();


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TextView textView = view.findViewById(R.id.rilevazioneText);

        Button interrompiButton= view.findViewById(R.id.rilevaButtonFragment);
        interrompiButton.setOnClickListener(v-> {
            rilevazioneService.stopRilevazione();
            //Intent intent = new Intent(this,HomePage.class);
            //startActivity(intent);

            fragmentTransaction.replace(R.id.fragmentContainerView2, RilevazioneFragment.class,null).
                    setReorderingAllowed(true).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

        });
        return view;
    }


}