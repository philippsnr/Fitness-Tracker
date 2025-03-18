package com.example.fitnesstracker.ui.exercise;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.MuscleGroup;
import com.example.fitnesstracker.repository.MuscleGroupRepository;
import com.example.fitnesstracker.viewmodel.ExerciseViewModel;

import java.util.List;

public class ExerciseFragment extends Fragment {
    private ExerciseViewModel exerciseViewModel;
    private RecyclerView recyclerView;
    private ExerciseAdapter exerciseAdapter;
    private LinearLayout buttonContainer;
    private MuscleGroupRepository muscleGroupRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Layout für das Fragment laden
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewExercises);
        buttonContainer = view.findViewById(R.id.buttonContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        createAdapter();
        loadMuscleGroups();
        return view;
    }

    protected void createAdapter(){
        exerciseAdapter = new ExerciseAdapter(getContext());
        recyclerView.setAdapter(exerciseAdapter);

        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        muscleGroupRepository = new MuscleGroupRepository(getContext());
    }

    private void loadMuscleGroups() {
        List<MuscleGroup> muscleGroups = muscleGroupRepository.getAllMuscleGroups();
        for (MuscleGroup mg : muscleGroups) {
            Button button = new Button(getContext());
            button.setText(mg.getName());
            button.setBackground(ContextCompat.getDrawable(button.getContext(), R.drawable.rounded_border_gray));
            button.setPadding(20, 0, 20, 0);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(10, 0, 10, 0); // Links, Oben, Rechts, Unten
            button.setLayoutParams(layoutParams);

            button.setOnClickListener(v -> loadExercisesForMuscleGroup(mg.getId()));
            buttonContainer.addView(button);
        }
    }

    private void loadExercisesForMuscleGroup(int muscleGroupId) {
        exerciseViewModel.loadExercisesForMuscleGroup(muscleGroupId, exercises -> {
            // Update der UI auf dem Haupt-Thread
            getActivity().runOnUiThread(() -> {
                exerciseAdapter.setExercises(exercises);
            });
        });
    }
}
