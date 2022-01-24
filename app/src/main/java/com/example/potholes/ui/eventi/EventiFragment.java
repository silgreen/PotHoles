package com.example.potholes.ui.eventi;

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

import com.example.potholes.R;

import java.util.ArrayList;

public class EventiFragment extends Fragment {

    private EventiViewModel mViewModel;
    private RecyclerView recyclerView;

    public static EventiFragment newInstance() {
        return new EventiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        ArrayList<String> s = new ArrayList<>();
        s.add("mela");
        s.add("ciccio");
        s.add("banan");
        View view = inflater.inflate(R.layout.eventi_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new CustomAdapter(s));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventiViewModel.class);
        // TODO: Use the ViewModel
    }

}