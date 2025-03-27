package com.example.fitnesstracker.ui.training;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainingExerciseFragment extends Fragment {

    private int trainingdayId;
    private String trainingdayName;
    private RecyclerView recyclerView;
    private TrainingExerciseAdapter adapter;
    private ExerciseViewModel exerciseViewModel;
    private TrainingdayExerciseAssignmentViewModel assignmentViewModel;
    // Mapping-Liste für die Assignment-IDs
    private List<Integer> assignmentIds = new ArrayList<>();

    /**
     * Erstellt eine neue Instanz des Fragments.
     *
     * @param trainingdayId   ID des Trainingstags.
     * @param trainingdayName Name des Trainingstags.
     * @return Neue Instanz von TrainingExerciseFragment.
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
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
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
        // Setzt den Titel des Fragments
        ((TextView) view.findViewById(R.id.tvTrainingDayNameHeading)).setText(trainingdayName);

        recyclerView = view.findViewById(R.id.recyclerViewTrainingdayExercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TrainingExerciseAdapter(new TrainingExerciseAdapter.OnItemClickListener() {
            @Override
            public void onCardClick(int position) {
                showExerciseSets(position);
            }

            @Override
            public void onDeleteClick(int position) {
                showRemoveConfirmationDialog(position);
            }
        });
        recyclerView.setAdapter(adapter);

        loadExercises();

        ImageView fab = getActivity().findViewById(R.id.addNewTrainingExercise);
        fab.setOnClickListener(v -> showAddExerciseDialog());
    }

    /**
     * Zeigt das TrainingSetsFragment für die ausgewählte Übung an.
     *
     * @param position Position der ausgewählten Übung im RecyclerView.
     */
    private void showExerciseSets(int position) {
        if (position < 0 || position >= assignmentIds.size()) return;

        int assignmentId = assignmentIds.get(position);

        exerciseViewModel.loadExercisesByIds(Collections.singletonList(assignmentId), exercises -> {
            if (!exercises.isEmpty()) {
                String exerciseName = exercises.get(0).getName(); // Name der Übung holen
                TrainingSetsFragment fragment = TrainingSetsFragment.newInstance(assignmentId, exerciseName);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    /**
     * Entfernt eine Übung anhand der übergebenen Position.
     *
     * @param position Position in der Liste der Übungszuweisungen.
     */
    private void removeTrainingExercise(int position) {
        if (position >= 0 && position < assignmentIds.size()) {
            int assignmentId = assignmentIds.get(position);
            assignmentViewModel.deleteTrainingdayExerciseAssignment(assignmentId, () ->
                    requireActivity().runOnUiThread(this::loadExercises)
            );
        }
    }

    /**
     * Zeigt einen Bestätigungsdialog an, um das Entfernen einer Übung zu bestätigen.
     *
     * @param position Position der Übung, die entfernt werden soll.
     */
    private void showRemoveConfirmationDialog(int position) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Löschen bestätigen")
                .setMessage("Möchten Sie diese Übung wirklich entfernen?")
                .setPositiveButton("Ja", (dialog, which) -> removeTrainingExercise(position))
                .setNegativeButton("Nein", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    /**
     * Lädt die Übungen für den aktuellen Trainingstag und aktualisiert die Ansicht.
     */
    private void loadExercises() {
        assignmentViewModel.getExerciseIdsForTrainingday(trainingdayId, exerciseIds -> {
            assignmentIds.clear();
            assignmentIds.addAll(exerciseIds);
            exerciseViewModel.loadExercisesByIds(exerciseIds, exercises -> {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> adapter.setExercises(exercises));
                }
            });
        });
    }

    /**
     * Zeigt einen Dialog zum Hinzufügen einer neuen Übung an.
     */
    private void showAddExerciseDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Neue Übung hinzufügen")
                .setMessage("Wähle die hinzuzufügende Übung aus")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Hier Backend-Logik einfügen
                })
                .setNegativeButton("Abbrechen", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
