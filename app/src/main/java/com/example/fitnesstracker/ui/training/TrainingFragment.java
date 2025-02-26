package com.example.fitnesstracker.ui.training;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.viewmodel.TrainingplanViewModel;

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

        // ViewModel initialisieren (empfohlen mit requireActivity(), falls von mehreren Fragmenten genutzt)
        viewModel = new ViewModelProvider(requireActivity()).get(TrainingplanViewModel.class);

        // Aktiven Trainingsplan beobachten
        viewModel.loadActiveTrainingplan(
                plan -> tvActivePlan.setText(plan != null ? plan.getName() : "Kein aktiver Plan"),
                error -> tvActivePlan.setText("Fehler beim Laden des Plans")
        );

        viewModel.loadAllTrainingplans(
                plans -> {
                    if (adapter == null) {
                        adapter = new TrainingplanAdapter(plans, plan ->
                                viewModel.setActiveTrainingplan(
                                        plan.getId(),  // 🔹 Hier die ID extrahieren!
                                        () -> System.out.println("Plan erfolgreich gesetzt"),
                                        error -> System.err.println("Fehler: " + error.getMessage())
                                )
                        );
                        rvTrainingPlans.setAdapter(adapter);
                    } else {
                        adapter.updatePlans(plans);
                    }
                },
                error -> System.err.println("Fehler beim Laden der Trainingspläne: " + error.getMessage())
        );


    }
}
