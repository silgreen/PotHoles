package com.example.potholes.ui.loading;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.DialogCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.potholes.HomePage;
import com.example.potholes.R;
import com.example.potholes.communication.SocketClient;
import com.example.potholes.services.EventiViciniService;
import com.example.potholes.services.PosizioneService;
import com.example.potholes.ui.eventi.EventiFragment;
import com.example.potholes.ui.eventi.EventiFragmentDirections;

public class LoadingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading,container,false);
        PosizioneService posizioneService = new PosizioneService(getContext());
        posizioneService.startLocation();
        SocketClient socketClient = new SocketClient(getContext());
        EventiViciniService eventiViciniService = EventiViciniService.getInstance(posizioneService,socketClient);
        eventiViciniService.startRequestEventiVicini();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NavDirections navDirections = LoadingFragmentDirections.actionLoadingFragmentToNavigationEventi();
                Navigation.findNavController(view).navigate(navDirections);
            }
        },4000);
        return view;
    }
}