package com.example.potholes.ui.loading;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private final Runnable changeFragment = new Runnable() {
        @Override
        public void run() {
            NavDirections navDirections = LoadingFragmentDirections.actionLoadingFragmentToNavigationEventi();
            Navigation.findNavController(container).navigate(navDirections);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading,container,false);
        this.container = container;
        startRequestEventiViciniOperations();
        createHandler();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(changeFragment);
    }

    public void startRequestEventiViciniOperations() {
        PosizioneService posizioneService = new PosizioneService(getContext());
        posizioneService.startLocation();
        SocketClient socketClient = new SocketClient(getContext());
        EventiViciniService eventiViciniService = EventiViciniService.getInstance(posizioneService,socketClient);
        eventiViciniService.startRequestEventiVicini();
    }

    public void createHandler() {
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(changeFragment,5000);
    }
}