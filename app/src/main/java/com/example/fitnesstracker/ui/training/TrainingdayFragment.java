package com.example.fitnesstracker.ui.training;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.viewmodel.TrainingdayViewModel;

import java.util.ArrayList;

public class TrainingdayFragment extends Fragment {

    private static final String ARG_TRAININGPLAN_ID = "trainingplan_id";
    private int trainingplanId;
    private TrainingdayViewModel viewModel;
    private RecyclerView recyclerView;
    private TrainingdayAdapter adapter;

    // Factory-Methode zur Erstellung eines Fragments mit Trainingsplan-ID
    public static TrainingdayFragment newInstance(int trainingplanId) {
        TrainingdayFragment fragment = new TrainingdayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRAININGPLAN_ID, trainingplanId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trainingplanId = getArguments().getInt(ARG_TRAININGPLAN_ID);
        }
        // Initialisiere das ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(TrainingdayViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Layout mit RecyclerView setzen
        View view = inflater.inflate(R.layout.fragment_trainingday, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewTrainingday);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TrainingdayAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Lade die Trainingstage für den übergebenen Trainingsplan
        viewModel.getTrainingdaysForPlan(trainingplanId, trainingdays ->
                requireActivity().runOnUiThread(() -> adapter.updateData(trainingdays))
        );
    }
}
