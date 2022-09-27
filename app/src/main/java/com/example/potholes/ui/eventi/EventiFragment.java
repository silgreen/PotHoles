package com.example.potholes.ui.eventi;

import android.content.Intent;
import android.location.Location;
import android.media.metrics.Event;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.potholes.R;
import com.example.potholes.communication.SocketClient;
import com.example.potholes.entity.Evento;
import com.example.potholes.services.EventiViciniService;
import com.example.potholes.services.PosizioneService;
import com.example.potholes.ui.loading.Container;
import com.example.potholes.ui.loading.LoadingDialogScreen;
import com.example.potholes.ui.loading.LoadingFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EventiFragment extends Fragment {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        List<Evento> [] eventiViciniList = new List[1];
        eventiViciniList[0] = new ArrayList<>();
        PosizioneService posizioneService = new PosizioneService(getContext());
        posizioneService.startLocation();
        SocketClient socketClient = new SocketClient(getContext());
        View view = inflater.inflate(R.layout.eventi_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewEventi);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        EventiViciniService eventiViciniService = new EventiViciniService(posizioneService,socketClient);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Evento>> futureList = executorService.submit(eventiViciniService.getCallable());
        EventiViewModel eventiViewModel = new ViewModelProvider(this).get(EventiViewModel.class);
        eventiViewModel.setEventoListVicini(eventiViciniList[0]);
        EventiAdapter eventiAdapter = new EventiAdapter(eventiViciniList[0]);
        recyclerView.setAdapter(eventiAdapter);
        if(eventiViciniList[0].isEmpty()) {
            Intent intent = new Intent(getActivity(),Container.class);
            startActivity(intent);
        }
        view.findViewById(R.id.cercaEventiViciniButton).setOnClickListener(view1 -> {
            try {
                eventiViciniList[0] = futureList.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            eventiViewModel.fillEventoList(eventiViciniList[0]);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    eventiViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Evento>>() {
                        @Override
                        public void onChanged(List<Evento> eventoList) {
                            if(recyclerView.getAdapter() != null) recyclerView.getAdapter().notifyItemInserted(eventoList.size());
                        }
                    });
                }
            });
        });
        return view;
        }

}