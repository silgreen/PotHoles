package com.example.potholes.ui.rilevazione;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.potholes.HomePage;
import com.example.potholes.R;
import com.example.potholes.entity.Evento;

public class RilevazioneFragment extends Fragment {

    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            Intent intent = new Intent(getActivity(), HomePage.class);
            startActivity(intent);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rilevazione_fragment, container, false);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),onBackPressedCallback);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRilevazione);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new RilevazioneEventiAdapter(Evento.EventoListClass.getEventoListRilevazione()));

        Button buttonRileva = view.findViewById(R.id.rilevaButtonFragment);
        buttonRileva.setOnClickListener(v -> fragmentTransaction.replace(R.id.fragmentContainerView2, RilevazioneStartedFragment.class,null).
                setReorderingAllowed(true).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit());

        return view;
    }
}