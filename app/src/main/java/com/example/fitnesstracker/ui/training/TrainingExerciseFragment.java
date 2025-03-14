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
import com.example.fitnesstracker.viewmodel.ExerciseViewModel;
import com.example.fitnesstracker.viewmodel.TrainingdayExerciseAssignmentViewModel;

public class TrainingExerciseFragment extends Fragment {
    private int trainingdayId;
    private String trainingdayName;
    private RecyclerView recyclerView;
    private TrainingExerciseAdapter adapter;
    private ExerciseViewModel exerciseViewModel;
    private TrainingdayExerciseAssignmentViewModel assignmentViewModel;

    public static TrainingExerciseFragment newInstance(int trainingdayId, String trainingdayName) {
        TrainingExerciseFragment fragment = new TrainingExerciseFragment();
        Bundle args = new Bundle();
        args.putInt("trainingdayId", trainingdayId);
        args.putString("trainingdayName", trainingdayName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trainingdayId = getArguments().getInt("trainingdayId");
            trainingdayName = getArguments().getString("trainingdayName");
        }
        // ViewModels initialisieren
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        assignmentViewModel = new ViewModelProvider(this).get(TrainingdayExerciseAssignmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Layout aus fragment_trainingexercises.xml aufblasen
        return inflater.inflate(R.layout.fragment_trainingexercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setze den Namen des Trainingstags
        TextView tvTitle = view.findViewById(R.id.tvTrainingExerciseTitle);
        tvTitle.setText(trainingdayName);

        recyclerView = view.findViewById(R.id.recyclerViewTrainingdayExercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TrainingExerciseAdapter();
        recyclerView.setAdapter(adapter);

        // Asynchron die Exercise-IDs laden und dann die zugehörigen Exercises abrufen
        assignmentViewModel.getExerciseIdsForTrainingday(trainingdayId, exerciseIds -> {
            exerciseViewModel.loadExercisesByIds(exerciseIds, exercises -> {
                // Update des Adapters im UI-Thread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        adapter.setExercises(exercises);
                        adapter.notifyDataSetChanged();
                    });
                }
            });
        });
    }
}
