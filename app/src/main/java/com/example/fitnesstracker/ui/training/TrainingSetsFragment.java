package com.example.fitnesstracker.ui.training;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.ExerciseSet;
import com.example.fitnesstracker.viewmodel.ExerciseSetViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment, das die Trainingssets (Sätze) einer Übung anzeigt.
 */
public class TrainingSetsFragment extends Fragment {
    private static final String ARG_ASSIGNMENT_ID = "assignment_id";
    private static final String ARG_EXERCISE_NAME = "exercise_name";

    private int assignmentId;
    private String exerciseName;

    private RecyclerView recyclerView;
    private TextView tvTrainingDayNameHeading;
    private TrainingSetsAdapter adapter;
    private ExerciseSetViewModel setViewModel;
    private CardView cardAddSet;
    private List<ExerciseSet> allSets = new ArrayList<>();

    /**
     * Erzeugt eine neue Instanz des Fragments.
     *
     * @param assignmentId Die ID der TrainingdayExerciseAssignment.
     * @param exerciseName Der Name der Übung.
     * @return Neue Instanz von TrainingSetsFragment.
     */
    public static TrainingSetsFragment newInstance(int assignmentId, String exerciseName) {
        TrainingSetsFragment fragment = new TrainingSetsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ASSIGNMENT_ID, assignmentId);
        args.putString(ARG_EXERCISE_NAME, exerciseName);
        fragment.setArguments(args);


        return fragment;
    }

    public interface OnExerciseSetCreatedListener {
        void onExerciseSetCreated(ExerciseSet exerciseSet);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assignmentId = getArguments().getInt(ARG_ASSIGNMENT_ID);
            exerciseName = getArguments().getString(ARG_EXERCISE_NAME);
        }
        setViewModel = new ViewModelProvider(this).get(ExerciseSetViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainingsets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // TextView für die Überschrift finden
        tvTrainingDayNameHeading = view.findViewById(R.id.tvTrainingDayNameHeading);
        tvTrainingDayNameHeading.setText(exerciseName); // Übungsnamen als Titel setzen

        recyclerView = view.findViewById(R.id.recyclerViewTrainingExerciseSets);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TrainingSetsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        cardAddSet = view.findViewById(R.id.cardAddSet);
        setupClickListeners();
        loadSets();
    }

    /**
     * Setzt die Click-Listener für interaktive Elemente
     */
    private void setupClickListeners() {
        cardAddSet.setOnClickListener(v -> handleAddSetClick());
    }

    private void handleAddSetClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Neuen Satz hinzufügen");

        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_add_set, null);

        TextInputEditText etReps = dialogView.findViewById(R.id.etReps);
        TextInputEditText etWeight = dialogView.findViewById(R.id.etWeight);
        TextInputLayout tilReps = dialogView.findViewById(R.id.tilReps);
        TextInputLayout tilWeight = dialogView.findViewById(R.id.tilWeight);

        builder.setView(dialogView)
                .setPositiveButton("Hinzufügen", (dialog, which) -> {
                    String repsStr = etReps.getText().toString().trim();
                    String weightStr = etWeight.getText().toString().trim();

                    if (validateInput(tilReps, tilWeight, repsStr, weightStr)) {
                        try {
                            createExerciseSet(repsStr, weightStr, new OnExerciseSetCreatedListener() {
                                @Override
                                public void onExerciseSetCreated(ExerciseSet newSet) {
                                    saveSetToViewModel(newSet);
                                    loadSets(); // Liste neu laden nach dem Speichern
                                }
                            });
                        } catch (NumberFormatException e) {
                            showNumberFormatError();
                        }
                    }
                })
                .setNegativeButton("Abbrechen", null);

        showDialogWithInputValidation(builder, etReps, etWeight);
    }

    private boolean validateInput(TextInputLayout tilReps,
                                  TextInputLayout tilWeight,
                                  String repsStr,
                                  String weightStr) {
        boolean isValid = true;

        // Reset errors
        tilReps.setError(null);
        tilWeight.setError(null);

        // Validate reps
        if (repsStr.isEmpty()) {
            tilReps.setError("Bitte Anzahl eingeben");
            isValid = false;
        }

        // Validate weight
        if (weightStr.isEmpty()) {
            tilWeight.setError("Bitte Gewicht eingeben");
            isValid = false;
        }

        return isValid;
    }

    private void createExerciseSet(String repsStr, String weightStr, OnExerciseSetCreatedListener listener) {
        LocalDate currentDate = LocalDate.now();
        String today = currentDate.toString();

        setViewModel.getLastSetNumber(today, assignmentId, new ExerciseSetViewModel.OnLastSetNumberLoadedListener() {
            @Override
            public void onLastSetNumberLoaded(int lastSetNumber) {
                ExerciseSet newSet = new ExerciseSet(
                        assignmentId,
                        lastSetNumber + 1,
                        Integer.parseInt(repsStr),
                        Double.parseDouble(weightStr),
                        today
                );
                listener.onExerciseSetCreated(newSet);
            }
        });
    }



    private void saveSetToViewModel(ExerciseSet newSet) {
        setViewModel.saveNewSet(newSet);
        loadSets(); // Annahme: Lädt die aktualisierte Liste
    }

    private void showNumberFormatError() {
        Toast.makeText(requireContext(),
                "Ungültige Zahlenangaben",
                Toast.LENGTH_SHORT).show();
    }

    private void showDialogWithInputValidation(AlertDialog.Builder builder,
                                               TextInputEditText etReps,
                                               TextInputEditText etWeight) {
        AlertDialog dialog = builder.create();
        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean repsValid = !etReps.getText().toString().trim().isEmpty();
                boolean weightValid = !etWeight.getText().toString().trim().isEmpty();
                positiveButton.setEnabled(repsValid && weightValid);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etReps.addTextChangedListener(textWatcher);
        etWeight.addTextChangedListener(textWatcher);
    }

    /**
     * Lädt die letzten Sets für die gegebene Assignment-ID und aktualisiert den Adapter.
     */
    private void loadSets() {
        setViewModel.loadLastSets(assignmentId, sets -> {
            allSets = sets; // Gespeicherte Sets aktualisieren
            requireActivity().runOnUiThread(() -> adapter.setData(groupByDate(sets)));
        });
    }

    /**
     * Gruppiert eine Liste von ExerciseSet nach Datum.
     *
     * @param sets Liste der geladenen ExerciseSet-Objekte.
     * @return Eine gruppierte Liste als List von TrainingSetsAdapter.ExerciseSetGroup.
     */
    private List<TrainingSetsAdapter.ExerciseSetGroup> groupByDate(List<ExerciseSet> sets) {
        List<TrainingSetsAdapter.ExerciseSetGroup> groups = new ArrayList<>();
        for (ExerciseSet set : sets) {
            boolean found = false;
            for (TrainingSetsAdapter.ExerciseSetGroup group : groups) {
                if (group.getDate().equals(set.getDate())) {
                    group.getSets().add(set);
                    found = true;
                    break;
                }
            }
            if (!found) {
                List<ExerciseSet> newGroupList = new ArrayList<>();
                newGroupList.add(set);
                groups.add(new TrainingSetsAdapter.ExerciseSetGroup(set.getDate(), newGroupList));
            }
        }
        return groups;
    }
}
