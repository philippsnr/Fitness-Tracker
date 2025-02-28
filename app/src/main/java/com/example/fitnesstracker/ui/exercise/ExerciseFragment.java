package com.example.fitnesstracker.ui.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.model.MuscleGroup;
import com.example.fitnesstracker.viewmodel.ExerciseViewModel;
import com.example.fitnesstracker.repository.MuscleGroupRepository;

import java.util.List;

public class ExerciseFragment extends Fragment {
    private ExerciseViewModel exerciseViewModel;
    private ExerciseAdapter exerciseAdapter;
    private LinearLayout buttonContainer;
    private MuscleGroupRepository muscleGroupRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewExercises);
        buttonContainer = view.findViewById(R.id.buttonContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        exerciseAdapter = new ExerciseAdapter();
        recyclerView.setAdapter(exerciseAdapter);

        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        muscleGroupRepository = new MuscleGroupRepository(getContext());

        loadMuscleGroups();
        return view;
    }

    private void loadMuscleGroups() {
        List<MuscleGroup> muscleGroups = muscleGroupRepository.getAllMuscleGroups();
        for (MuscleGroup mg : muscleGroups) {
            Button button = new Button(getContext());
            button.setText(mg.getName());
            button.setOnClickListener(v -> loadExercisesForMuscleGroup(mg.getId()));
            buttonContainer.addView(button);
        }
    }

    private void loadExercisesForMuscleGroup(int muscleGroupId) {
        exerciseViewModel.loadExercisesForMuscleGroup(muscleGroupId, exercises -> {
            getActivity().runOnUiThread(() -> {
                exerciseAdapter.setExercises(exercises);
            });
        });
    }
}
