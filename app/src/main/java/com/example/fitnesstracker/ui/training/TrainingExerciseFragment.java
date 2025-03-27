package com.example.fitnesstracker.ui.training;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.model.MuscleGroup;
import com.example.fitnesstracker.viewmodel.ExerciseViewModel;
import com.example.fitnesstracker.viewmodel.MuscleGroupViewModel;
import com.example.fitnesstracker.viewmodel.TrainingdayExerciseAssignmentViewModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class TrainingExerciseFragment extends Fragment {

    private int trainingdayId;
    private String trainingdayName;
    private RecyclerView recyclerView;
    private TrainingExerciseAdapter adapter;
    private ExerciseViewModel exerciseViewModel;
    private TrainingdayExerciseAssignmentViewModel assignmentViewModel;
    private MuscleGroupViewModel muscleGroupViewModel;
    private List<Integer> assignmentIds = new ArrayList<>();

    private static final String TAG = "TrainingExerciseFragment";

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
        muscleGroupViewModel = new ViewModelProvider(this).get(MuscleGroupViewModel.class);
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

    private void showAddExerciseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Muskelgruppe wählen");

        View loadingView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_loading, null);
        builder.setView(loadingView);
        AlertDialog loadingDialog = builder.create();
        loadingDialog.show();

        // Verwende den Listener direkt aus dem ViewModel
        muscleGroupViewModel.loadMuscleGroups(new MuscleGroupViewModel.OnDataLoadedListener<List<MuscleGroup>>() {
            @Override
            public void onDataLoaded(List<MuscleGroup> muscleGroups) {  // Expliziter Typ
                requireActivity().runOnUiThread(() -> {
                    loadingDialog.dismiss();

                    if (muscleGroups == null || muscleGroups.isEmpty()) {
                        showErrorDialog("Keine Muskelgruppen gefunden");
                        return;
                    }

                    String[] muscleGroupNames = new String[muscleGroups.size()];
                    for (int i = 0; i < muscleGroups.size(); i++) {
                        muscleGroupNames[i] = muscleGroups.get(i).getName();
                    }

                    new AlertDialog.Builder(requireContext())
                            .setItems(muscleGroupNames, (dialog, which) -> {
                                int selectedMuscleGroupId = muscleGroups.get(which).getId();
                                showExercisesForMuscleGroup(selectedMuscleGroupId);
                            })
                            .show();
                });
            }
        });
    }

    private void showExercisesForMuscleGroup(int muscleGroupId) {
        Log.d(TAG, "showExercisesForMuscleGroup() muscleGroupId: " + muscleGroupId);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Übung auswählen");

        View loadingView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_loading, null);
        builder.setView(loadingView);
        AlertDialog loadingDialog = builder.create();
        loadingDialog.show();

        muscleGroupViewModel.loadExercisesForMuscleGroup(muscleGroupId, new MuscleGroupViewModel.OnDataLoadedListener<List<Exercise>>() {
            @Override
            public void onDataLoaded(List<Exercise> exercises) {
                Log.d(TAG, "onDataLoaded() exercises count: " + (exercises != null ? exercises.size() : 0));

                requireActivity().runOnUiThread(() -> {
                    loadingDialog.dismiss();

                    if (exercises == null || exercises.isEmpty()) {
                        Log.e(TAG, "Keine Übungen gefunden für muscleGroupId: " + muscleGroupId);
                        showErrorDialog("Keine Übungen für diese Muskelgruppe");
                        return;
                    }

                    // Logge alle geladenen Übungen
                    for (Exercise exercise : exercises) {
                        Log.d(TAG, "Geladene Übung - ID: " + exercise.getId()
                                + ", Name: " + exercise.getName());
                    }

                    // Erstelle lokale Kopie für Konsistenz
                    final List<Exercise> dialogExercises = new ArrayList<>(exercises);

                    String[] exerciseNames = new String[dialogExercises.size()];
                    for (int i = 0; i < dialogExercises.size(); i++) {
                        exerciseNames[i] = dialogExercises.get(i).getName();
                    }

                    new AlertDialog.Builder(requireContext())
                            .setTitle("Übung auswählen")
                            .setItems(exerciseNames, (dialog, which) -> {
                                Log.d(TAG, "Ausgewählte Position: " + which);

                                if (which >= 0 && which < dialogExercises.size()) {
                                    Exercise selectedExercise = dialogExercises.get(which);
                                    Log.d(TAG, "Ausgewählte Übung - ID: " + selectedExercise.getId()
                                            + ", Name: " + selectedExercise.getName());

                                    addExerciseToTrainingDay(selectedExercise.getId());
                                } else {
                                    Log.e(TAG, "Ungültige Position: " + which);
                                }
                            })
                            .setNegativeButton("Zurück", (d, w) -> showAddExerciseDialog())
                            .show();
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadExercises(); // Daten bei jedem sichtbar werden neu laden
    }

    private void addExerciseToTrainingDay(int exerciseId) {
        Log.d("FRAGMENT", "Start adding exerciseId: "+exerciseId+" to trainingday: "+trainingdayId);

        assignmentViewModel.addTrainingExerciseAssignment(trainingdayId, exerciseId, newId -> {
            requireActivity().runOnUiThread(() -> {
                if (newId != -1) {
                    Log.d("FRAGMENT", "Success! New assignment ID: "+newId);
                    loadExercises();
                } else {
                    Log.e("FRAGMENT", "Failed to add exercise");
                    Toast.makeText(getContext(), "Speichern fehlgeschlagen", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Fehler")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
