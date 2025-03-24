package com.example.fitnesstracker.ui.training;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.viewmodel.ExerciseViewModel;
import com.example.fitnesstracker.viewmodel.TrainingdayExerciseAssignmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

/**
 * Fragment zur Anzeige und Verwaltung der Übungen eines Trainingstags.
 */
public class TrainingExerciseFragment extends Fragment {

    private int trainingdayId;
    private String trainingdayName;
    private RecyclerView recyclerView;
    private TrainingExerciseAdapter adapter;
    private ExerciseViewModel exerciseViewModel;
    private TrainingdayExerciseAssignmentViewModel assignmentViewModel;

    /**
     * Erzeugt eine neue Instanz des Fragments.
     */
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
        if (exerciseViewModel == null)
            exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        if (assignmentViewModel == null)
            assignmentViewModel = new ViewModelProvider(this).get(TrainingdayExerciseAssignmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainingexercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.tvTrainingExerciseTitle)).setText(trainingdayName);
        recyclerView = view.findViewById(R.id.recyclerViewTrainingdayExercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TrainingExerciseAdapter(new TrainingExerciseAdapter.OnItemClickListener() {
            @Override
            public void onCardClick(int position) {
                // Hier Backend-Logik für Detailansicht implementieren
                Toast.makeText(getContext(), "Karte geklickt: " + position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDeleteClick(int position) {
                // Hier Backend-Logik zum Löschen implementieren
                Toast.makeText(getContext(), "Löschen angeklickt: " + position, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        loadExercises();
        // Plus-Button (zum Hinzufügen) vorbereiten
        ImageView fab = getActivity().findViewById(R.id.addNewTrainingExercise);
        fab.setOnClickListener(v -> {
            // Hier Backend-Logik für das Hinzufügen implementieren
            showAddExerciseDialog();
        });
    }

    /** Lädt Übungen für den Trainingstag. */
    private void loadExercises() {
        assignmentViewModel.getExerciseIdsForTrainingday(trainingdayId, exerciseIds ->
                exerciseViewModel.loadExercisesByIds(exerciseIds, exercises ->
                        getActivity().runOnUiThread(() -> adapter.setExercises(exercises))));
    }

    /** Zeigt einen Dialog zum Hinzufügen einer neuen Übung an. */
    private void showAddExerciseDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Neue Übung hinzufügen")
                .setMessage("Hier Dialog implementieren")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Backend-Logik hier einfügen
                })
                .setNegativeButton("Abbrechen", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
