package com.example.potholes.ui.rilevazione;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.potholes.R;
import com.example.potholes.RilevazioneEventiAdapter;
import com.example.potholes.communication.SocketClient;
import com.example.potholes.ui.eventi.CustomAdapter;

public class RilevazioneFragment extends Fragment {

    private RilevazioneViewModel mViewModel;

    public static RilevazioneFragment newInstance() {
        return new RilevazioneFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rilevazione_fragment, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRilevazione);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new RilevazioneEventiAdapter(new String[]{"mela", "lai","ciccio"}));

        Button buttonRileva = view.findViewById(R.id.rilevaButtonFragment);
        buttonRileva.setOnClickListener(v -> {
            fragmentTransaction.replace(R.id.fragmentContainerView2, RilevazioneStartedFragment.class,null).
                    setReorderingAllowed(true).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
        });

        return view;
    }

}