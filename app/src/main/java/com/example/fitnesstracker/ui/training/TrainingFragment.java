package com.example.fitnesstracker.ui.training;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.viewmodel.TrainingplanViewModel;
import com.example.fitnesstracker.model.Trainingplan;

import java.util.ArrayList;

public class TrainingFragment extends Fragment {

    private TrainingplanViewModel viewModel;
    private TrainingplanAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvActivePlan = view.findViewById(R.id.tvActivePlan);
        RecyclerView rvTrainingPlans = view.findViewById(R.id.rvTrainingPlans);
        rvTrainingPlans.setLayoutManager(new LinearLayoutManager(requireContext()));

        // ViewModel initialisieren
        viewModel = new ViewModelProvider(requireActivity()).get(TrainingplanViewModel.class);

        // Adapter initialisieren und setzen
        adapter = new TrainingplanAdapter(new ArrayList<>(), plan -> {
            if (plan != null) {
                viewModel.setActiveTrainingplan(plan.getId(),
                        () -> System.out.println("Plan erfolgreich gesetzt"),
                        error -> System.err.println("Fehler: " + error.getMessage()));
            }
        });
        rvTrainingPlans.setAdapter(adapter);

        // Aktiven Trainingsplan beobachten
        viewModel.loadActiveTrainingplan(
                plan -> {
                    if (plan != null) {
                        tvActivePlan.setText(plan.getName());
                    } else {
                        tvActivePlan.setText("Kein aktiver Plan");
                    }
                },
                error -> {
                    System.err.println("Fehler beim Laden des aktiven Plans: " + error.getMessage());
                }
        );

        // Liste aller Trainingspläne beobachten
        viewModel.loadAllTrainingplans(
                plans -> requireActivity().runOnUiThread(() -> {
                    if (adapter == null) {
                        adapter = new TrainingplanAdapter(plans, plan ->
                                viewModel.setActiveTrainingplan(
                                        plan.getId(),
                                        () -> System.out.println("Plan erfolgreich gesetzt"),
                                        error -> System.err.println("Fehler: " + error.getMessage())
                                )
                        );
                        rvTrainingPlans.setAdapter(adapter);
                    } else {
                        adapter.updatePlans(plans);
                    }
                }),
                error -> requireActivity().runOnUiThread(() ->
                        System.err.println("Fehler beim Laden der Trainingspläne: " + error.getMessage())
                )
        );

    }
}
