package com.example.fitnesstracker.ui.training;

import android.app.AlertDialog;
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
import com.example.fitnesstracker.model.TrainingdayExerciseAssignment;
import com.example.fitnesstracker.viewmodel.ExerciseViewModel;
import com.example.fitnesstracker.viewmodel.MuscleGroupViewModel;
import com.example.fitnesstracker.viewmodel.TrainingdayExerciseAssignmentViewModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment zur Anzeige und Verwaltung von Übungen eines Trainingstages.
 * Ermöglicht das Hinzufügen, Anzeigen und Löschen von Übungen.
 */
public class TrainingExerciseFragment extends Fragment {
    private static final String TAG = "TrainingExerciseFragment";
    private static final String TRAININGDAY_ID_KEY = "trainingdayId";
    private static final String TRAININGDAY_NAME_KEY = "trainingdayName";

    private int trainingdayId;
    private String trainingdayName;
    private RecyclerView recyclerView;
    private TrainingExerciseAdapter adapter;
    private ExerciseViewModel exerciseViewModel;
    private TrainingdayExerciseAssignmentViewModel assignmentViewModel;
    private MuscleGroupViewModel muscleGroupViewModel;
    private final List<TrainingdayExerciseAssignment> assignments = new ArrayList<>();

    /**
     * Erstellt eine neue Instanz des Fragments.
     * @param trainingdayId ID des Trainingstages
     * @param trainingdayName Name des Trainingstages
     * @return Neue Instanz des Fragments
     */
    public static TrainingExerciseFragment newInstance(int trainingdayId, String trainingdayName) {
        TrainingExerciseFragment fragment = new TrainingExerciseFragment();
        Bundle args = new Bundle();
        args.putInt(TRAININGDAY_ID_KEY, trainingdayId);
        args.putString(TRAININGDAY_NAME_KEY, trainingdayName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadArgumentsFromBundle();
        initializeViewModels();
    }

    /**
     * Lädt die Argumente aus dem Bundle.
     */
    private void loadArgumentsFromBundle() {
        if (getArguments() != null) {
            trainingdayId = getArguments().getInt(TRAININGDAY_ID_KEY);
            trainingdayName = getArguments().getString(TRAININGDAY_NAME_KEY);
        }
    }

    /**
     * Initialisiert die ViewModels.
     */
    private void initializeViewModels() {
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
        initializeViews(view);
        setupRecyclerView();
        setupFabClickListener();
        loadExercises();
    }

    /**
     * Initialisiert die Views.
     * @param view Die Root-View des Fragments
     */
    private void initializeViews(View view) {
        ((TextView) view.findViewById(R.id.tvTrainingDayNameHeading)).setText(trainingdayName);
        recyclerView = view.findViewById(R.id.recyclerViewTrainingdayExercises);
    }

    /**
     * Konfiguriert den RecyclerView.
     */
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = createAdapter();
        recyclerView.setAdapter(adapter);
    }

    /**
     * Erstellt den Adapter für den RecyclerView.
     * @return Den erstellten Adapter
     */
    private TrainingExerciseAdapter createAdapter() {
        return new TrainingExerciseAdapter(new TrainingExerciseAdapter.OnItemClickListener() {
            @Override
            public void onCardClick(int position) {
                showExerciseSets(position);
            }

            @Override
            public void onDeleteClick(int position) {
                showRemoveConfirmationDialog(position);
            }
        });
    }

    /**
     * Setzt den Klick-Listener für den FAB (Floating Action Button).
     */
    private void setupFabClickListener() {
        ImageView fab = requireActivity().findViewById(R.id.addNewTrainingExercise);
        fab.setOnClickListener(v -> showAddExerciseDialog());
    }

    /**
     * Zeigt die Sätze einer Übung an.
     * @param position Position der Übung in der Liste
     */
    private void showExerciseSets(int position) {
        if (position < 0 || position >= assignments.size()) return;

        TrainingdayExerciseAssignment assignment = assignments.get(position);
        loadExerciseNameAndShowFragment(assignment);
    }

    /**
     * Lädt den Übungsnamen und zeigt das Sets-Fragment an.
     * @param assignment Die Übungszuordnung
     */
    private void loadExerciseNameAndShowFragment(TrainingdayExerciseAssignment assignment) {
        exerciseViewModel.loadExercisesByIds(
                Collections.singletonList(assignment.getExerciseId()),
                exercises -> {
                    if (!exercises.isEmpty()) {
                        String exerciseName = exercises.get(0).getName();
                        TrainingSetsFragment fragment = TrainingSetsFragment.newInstance(
                                assignment.getId(),
                                exerciseName
                        );
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
        );
    }

    /**
     * Entfernt eine Übung vom Trainingstag.
     * @param position Position der zu entfernenden Übung
     */
    private void removeTrainingExercise(int position) {
        if (position >= 0 && position < assignments.size()) {
            int assignmentId = assignments.get(position).getId();
            Log.d(TAG, "Attempting to delete assignment ID: " + assignmentId);

            assignmentViewModel.deleteTrainingdayExerciseAssignment(assignmentId, () -> {
                Log.d(TAG, "Successfully deleted assignment ID: " + assignmentId);
                requireActivity().runOnUiThread(this::loadExercises);
            });
        }
    }

    /**
     * Zeigt einen Bestätigungsdialog zum Löschen einer Übung.
     * @param position Position der zu löschenden Übung
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
     * Lädt die Übungen für den Trainingstag.
     */
    private void loadExercises() {
        Log.d(TAG, "Loading exercises for trainingday ID: " + trainingdayId);
        assignmentViewModel.getAssignmentsForTrainingday(
                trainingdayId,
                this::handleLoadedAssignments
        );
    }

    /**
     * Verarbeitet die geladenen Übungszuordnungen.
     * @param newAssignments Liste der geladenen Übungszuordnungen
     */
    private void handleLoadedAssignments(List<TrainingdayExerciseAssignment> newAssignments) {
        Log.d(TAG, "Loaded " + newAssignments.size() + " assignments");

        assignments.clear();
        assignments.addAll(newAssignments);

        List<Integer> exerciseIds = new ArrayList<>();
        for (TrainingdayExerciseAssignment assignment : newAssignments) {
            exerciseIds.add(assignment.getExerciseId());
        }

        exerciseViewModel.loadExercisesByIds(exerciseIds, exercises -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter.setExercises(exercises);
                    adapter.setAssignments(newAssignments);
                });
            }
        });
    }

    /**
     * Zeigt den Dialog zum Hinzufügen einer neuen Übung.
     */
    private void showAddExerciseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Muskelgruppe wählen");

        AlertDialog loadingDialog = showLoadingDialog(builder);
        loadMuscleGroups(loadingDialog);
    }

    /**
     * Zeigt einen Lade-Dialog an.
     * @param builder Der Dialog-Builder
     * @return Den erstellten Dialog
     */
    private AlertDialog showLoadingDialog(AlertDialog.Builder builder) {
        View loadingView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_loading, null);
        builder.setView(loadingView);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * Lädt die Muskelgruppen.
     * @param loadingDialog Der anzuzeigende Lade-Dialog
     */
    private void loadMuscleGroups(AlertDialog loadingDialog) {
        muscleGroupViewModel.loadMuscleGroups(muscleGroups -> requireActivity().runOnUiThread(() -> {
            loadingDialog.dismiss();
            handleLoadedMuscleGroups(muscleGroups);
        }));
    }

    /**
     * Verarbeitet die geladenen Muskelgruppen.
     * @param muscleGroups Liste der geladenen Muskelgruppen
     */
    private void handleLoadedMuscleGroups(List<MuscleGroup> muscleGroups) {
        if (muscleGroups == null || muscleGroups.isEmpty()) {
            showErrorDialog("Keine Muskelgruppen gefunden");
            return;
        }

        showMuscleGroupSelectionDialog(muscleGroups);
    }

    /**
     * Zeigt einen Dialog zur Auswahl einer Muskelgruppe.
     * @param muscleGroups Liste der verfügbaren Muskelgruppen
     */
    private void showMuscleGroupSelectionDialog(List<MuscleGroup> muscleGroups) {
        String[] muscleGroupNames = createMuscleGroupNamesArray(muscleGroups);
        new AlertDialog.Builder(requireContext())
                .setItems(muscleGroupNames, (dialog, which) -> {
                    int selectedMuscleGroupId = muscleGroups.get(which).getId();
                    showExercisesForMuscleGroup(selectedMuscleGroupId);
                })
                .show();
    }

    /**
     * Erstellt ein Array mit Muskelgruppennamen.
     * @param muscleGroups Liste der Muskelgruppen
     * @return Array mit den Namen der Muskelgruppen
     */
    private String[] createMuscleGroupNamesArray(List<MuscleGroup> muscleGroups) {
        String[] names = new String[muscleGroups.size()];
        for (int i = 0; i < muscleGroups.size(); i++) {
            names[i] = muscleGroups.get(i).getName();
        }
        return names;
    }

    /**
     * Zeigt die Übungen für eine ausgewählte Muskelgruppe.
     * @param muscleGroupId ID der ausgewählten Muskelgruppe
     */
    private void showExercisesForMuscleGroup(int muscleGroupId) {
        Log.d(TAG, "Loading exercises for muscle group ID: " + muscleGroupId);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Übung auswählen");

        AlertDialog loadingDialog = showLoadingDialog(builder);
        loadExercisesForMuscleGroup(muscleGroupId, loadingDialog);
    }

    /**
     * Lädt die Übungen für eine Muskelgruppe.
     * @param muscleGroupId ID der Muskelgruppe
     * @param loadingDialog Der anzuzeigende Lade-Dialog
     */
    private void loadExercisesForMuscleGroup(int muscleGroupId, AlertDialog loadingDialog) {
        muscleGroupViewModel.loadExercisesForMuscleGroup(
                muscleGroupId,
                exercises -> requireActivity().runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    handleLoadedExercises(muscleGroupId, exercises);
                })
        );
    }

    /**
     * Verarbeitet die geladenen Übungen.
     * @param muscleGroupId ID der Muskelgruppe
     * @param exercises Liste der geladenen Übungen
     */
    private void handleLoadedExercises(int muscleGroupId, List<Exercise> exercises) {
        if (exercises == null || exercises.isEmpty()) {
            Log.e(TAG, "No exercises found for muscleGroupId: " + muscleGroupId);
            showErrorDialog("Keine Übungen für diese Muskelgruppe");
            return;
        }

        showExerciseSelectionDialog(new ArrayList<>(exercises));
    }

    /**
     * Zeigt einen Dialog zur Auswahl einer Übung.
     * @param exercises Liste der verfügbaren Übungen
     */
    private void showExerciseSelectionDialog(List<Exercise> exercises) {
        String[] exerciseNames = createExerciseNamesArray(exercises);
        new AlertDialog.Builder(requireContext())
                .setTitle("Übung auswählen")
                .setItems(exerciseNames, (dialog, which) -> handleExerciseSelection(which, exercises))
                .setNegativeButton("Zurück", (d, w) -> showAddExerciseDialog())
                .show();
    }

    /**
     * Erstellt ein Array mit Übungsnamen.
     * @param exercises Liste der Übungen
     * @return Array mit den Namen der Übungen
     */
    private String[] createExerciseNamesArray(List<Exercise> exercises) {
        String[] names = new String[exercises.size()];
        for (int i = 0; i < exercises.size(); i++) {
            names[i] = exercises.get(i).getName();
        }
        return names;
    }

    /**
     * Verarbeitet die Auswahl einer Übung.
     * @param which Index der ausgewählten Übung
     * @param exercises Liste der verfügbaren Übungen
     */
    private void handleExerciseSelection(int which, List<Exercise> exercises) {
        if (which >= 0 && which < exercises.size()) {
            Exercise selectedExercise = exercises.get(which);
            addExerciseToTrainingDay(selectedExercise.getId());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadExercises();
    }

    /**
     * Fügt eine Übung zum Trainingstag hinzu.
     * @param exerciseId ID der hinzuzufügenden Übung
     */
    private void addExerciseToTrainingDay(int exerciseId) {
        Log.d(TAG, "Adding exercise ID: " + exerciseId + " to trainingday: " + trainingdayId);

        assignmentViewModel.addTrainingExerciseAssignment(
                trainingdayId,
                exerciseId,
                newId -> handleAssignmentResult(newId.intValue())
        );
    }

    /**
     * Verarbeitet das Ergebnis der Übungszuordnung.
     * @param newId ID der neuen Zuordnung (-1 bei Fehler)
     */
    private void handleAssignmentResult(int newId) {
        requireActivity().runOnUiThread(() -> {
            if (newId != -1) {
                Log.d(TAG, "Successfully added assignment ID: " + newId);
                loadExercises();
            } else {
                Log.e(TAG, "Failed to add exercise");
                showSaveErrorToast();
            }
        });
    }

    /**
     * Zeigt eine Fehlermeldung beim Speichern an.
     */
    private void showSaveErrorToast() {
        Toast.makeText(getContext(), "Speichern fehlgeschlagen", Toast.LENGTH_SHORT).show();
    }

    /**
     * Zeigt einen Fehlerdialog an.
     * @param message Die anzuzeigende Fehlermeldung
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Fehler")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}