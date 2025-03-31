package com.example.fitnesstracker.ui.training;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        loadArgumentsFromBundle();
        initializeViewModel();
    }

    /**
     * Lädt die übergebenen Argumente aus dem Bundle.
     */
    private void loadArgumentsFromBundle() {
        if (getArguments() != null) {
            assignmentId = getArguments().getInt(ARG_ASSIGNMENT_ID);
            exerciseName = getArguments().getString(ARG_EXERCISE_NAME);
        }
    }

    /**
     * Initialisiert das ViewModel für die ExerciseSets.
     */
    private void initializeViewModel() {
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
        initializeViews(view);
        setupRecyclerView();
        setupClickListeners();
        loadSets();
    }

    /**
     * Initialisiert die UI-Elemente.
     *
     * @param view Die Root-View des Fragments.
     */
    private void initializeViews(View view) {
        tvTrainingDayNameHeading = view.findViewById(R.id.tvTrainingDayNameHeading);
        tvTrainingDayNameHeading.setText(exerciseName);
        recyclerView = view.findViewById(R.id.recyclerViewTrainingExerciseSets);
        cardAddSet = view.findViewById(R.id.cardAddSet);
    }

    /**
     * Konfiguriert den RecyclerView mit LayoutManager und Adapter.
     */
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TrainingSetsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    /**
     * Setzt die Click-Listener für interaktive Elemente.
     */
    private void setupClickListeners() {
        cardAddSet.setOnClickListener(v -> handleAddSetClick());
    }

    /**
     * Behandelt den Klick auf den "Add Set" Button.
     */
    private void handleAddSetClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Neuen Satz hinzufügen");
        View dialogView = createDialogView();
        builder.setView(dialogView);
        setupDialogButtons(builder, dialogView);
        AlertDialog dialog = builder.create();
        setupDialogInputValidation(dialog, dialogView);
        dialog.show();
    }

    /**
     * Erstellt die Dialog-View für das Hinzufügen eines neuen Sets.
     *
     * @return Die erstellte Dialog-View.
     */
    private View createDialogView() {
        return LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_add_set, null);
    }

    /**
     * Konfiguriert die Dialog-Buttons und deren Logik.
     *
     * @param builder Der AlertDialog.Builder.
     * @param dialogView Die Dialog-View.
     */
    private void setupDialogButtons(AlertDialog.Builder builder, View dialogView) {
        TextInputEditText etReps = dialogView.findViewById(R.id.etReps);
        TextInputEditText etWeight = dialogView.findViewById(R.id.etWeight);
        TextInputLayout tilReps = dialogView.findViewById(R.id.tilReps);
        TextInputLayout tilWeight = dialogView.findViewById(R.id.tilWeight);

        builder.setPositiveButton("Hinzufügen", (dialog, which) ->
                handlePositiveButtonClick(etReps, etWeight, tilReps, tilWeight));
        builder.setNegativeButton("Abbrechen", null);
    }

    /**
     * Behandelt den Klick auf den positiven Button im Dialog.
     *
     * @param etReps Das Eingabefeld für Wiederholungen.
     * @param etWeight Das Eingabefeld für Gewicht.
     * @param tilReps Das TextInputLayout für Wiederholungen.
     * @param tilWeight Das TextInputLayout für Gewicht.
     */
    private void handlePositiveButtonClick(TextInputEditText etReps, TextInputEditText etWeight,
                                           TextInputLayout tilReps, TextInputLayout tilWeight) {
        String repsStr = etReps.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (validateInput(tilReps, tilWeight, repsStr, weightStr)) {
            try {
                createAndSaveExerciseSet(repsStr, weightStr);
            } catch (NumberFormatException e) {
                showNumberFormatError();
            }
        }
    }

    /**
     * Erstellt und speichert einen neuen ExerciseSet.
     *
     * @param repsStr Die Anzahl der Wiederholungen als String.
     * @param weightStr Das Gewicht als String.
     */
    private void createAndSaveExerciseSet(String repsStr, String weightStr) {
        createExerciseSet(repsStr, weightStr, newSet -> {
            saveSetToViewModel(newSet);
            loadSets();
        });
    }

    /**
     * Konfiguriert die Eingabevalidierung für den Dialog.
     *
     * @param dialog Der AlertDialog.
     * @param dialogView Die Dialog-View.
     */
    private void setupDialogInputValidation(AlertDialog dialog, View dialogView) {
        TextInputEditText etReps = dialogView.findViewById(R.id.etReps);
        TextInputEditText etWeight = dialogView.findViewById(R.id.etWeight);
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        TextWatcher textWatcher = createTextWatcher(etReps, etWeight, positiveButton);
        etReps.addTextChangedListener(textWatcher);
        etWeight.addTextChangedListener(textWatcher);
    }

    /**
     * Erstellt einen TextWatcher für die Eingabevalidierung.
     *
     * @param etReps Das Eingabefeld für Wiederholungen.
     * @param etWeight Das Eingabefeld für Gewicht.
     * @param positiveButton Der positive Button des Dialogs.
     * @return Der erstellte TextWatcher.
     */
    private TextWatcher createTextWatcher(TextInputEditText etReps, TextInputEditText etWeight,
                                          Button positiveButton) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateDialogInput(etReps, etWeight, positiveButton);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    /**
     * Validiert die Eingaben im Dialog und aktiviert/deaktiviert den Button entsprechend.
     *
     * @param etReps Das Eingabefeld für Wiederholungen.
     * @param etWeight Das Eingabefeld für Gewicht.
     * @param positiveButton Der positive Button des Dialogs.
     */
    private void validateDialogInput(TextInputEditText etReps, TextInputEditText etWeight,
                                     Button positiveButton) {
        boolean repsValid = !etReps.getText().toString().trim().isEmpty();
        boolean weightValid = !etWeight.getText().toString().trim().isEmpty();
        positiveButton.setEnabled(repsValid && weightValid);
    }

    /**
     * Validiert die Benutzereingaben für Wiederholungen und Gewicht.
     *
     * @param tilReps Das TextInputLayout für Wiederholungen.
     * @param tilWeight Das TextInputLayout für Gewicht.
     * @param repsStr Die eingegebenen Wiederholungen als String.
     * @param weightStr Das eingegebene Gewicht als String.
     * @return true, wenn die Eingaben valide sind, sonst false.
     */
    private boolean validateInput(TextInputLayout tilReps, TextInputLayout tilWeight,
                                  String repsStr, String weightStr) {
        resetInputErrors(tilReps, tilWeight);
        return checkRepsValid(tilReps, repsStr) && checkWeightValid(tilWeight, weightStr);
    }

    /**
     * Setzt die Fehleranzeigen der Eingabefelder zurück.
     *
     * @param tilReps Das TextInputLayout für Wiederholungen.
     * @param tilWeight Das TextInputLayout für Gewicht.
     */
    private void resetInputErrors(TextInputLayout tilReps, TextInputLayout tilWeight) {
        tilReps.setError(null);
        tilWeight.setError(null);
    }

    /**
     * Überprüft die Gültigkeit der Wiederholungs-Eingabe.
     *
     * @param tilReps Das TextInputLayout für Wiederholungen.
     * @param repsStr Die eingegebenen Wiederholungen als String.
     * @return true, wenn die Eingabe valide ist, sonst false.
     */
    private boolean checkRepsValid(TextInputLayout tilReps, String repsStr) {
        if (repsStr.isEmpty()) {
            tilReps.setError("Bitte Anzahl eingeben");
            return false;
        }
        return true;
    }

    /**
     * Überprüft die Gültigkeit der Gewichts-Eingabe.
     *
     * @param tilWeight Das TextInputLayout für Gewicht.
     * @param weightStr Das eingegebene Gewicht als String.
     * @return true, wenn die Eingabe valide ist, sonst false.
     */
    private boolean checkWeightValid(TextInputLayout tilWeight, String weightStr) {
        if (weightStr.isEmpty()) {
            tilWeight.setError("Bitte Gewicht eingeben");
            return false;
        }
        return true;
    }

    /**
     * Erstellt einen neuen ExerciseSet.
     *
     * @param repsStr Die Anzahl der Wiederholungen als String.
     * @param weightStr Das Gewicht als String.
     * @param listener Callback für den Erfolgsfall.
     */
    private void createExerciseSet(String repsStr, String weightStr,
                                   OnExerciseSetCreatedListener listener) {
        LocalDate currentDate = LocalDate.now();
        String today = currentDate.toString();

        setViewModel.getLastSetNumber(today, assignmentId, lastSetNumber -> {
            ExerciseSet newSet = new ExerciseSet(
                    assignmentId,
                    lastSetNumber + 1,
                    Integer.parseInt(repsStr),
                    Double.parseDouble(weightStr),
                    today
            );
            listener.onExerciseSetCreated(newSet);
        });
    }

    /**
     * Speichert einen neuen ExerciseSet im ViewModel.
     *
     * @param newSet Der zu speichernde ExerciseSet.
     */
    private void saveSetToViewModel(ExerciseSet newSet) {
        setViewModel.saveNewSet(newSet);
    }

    /**
     * Zeigt eine Fehlermeldung bei ungültiger Zahlenangabe an.
     */
    private void showNumberFormatError() {
        Toast.makeText(requireContext(),
                "Ungültige Zahlenangaben",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Lädt die letzten Sets für die gegebene Assignment-ID und aktualisiert den Adapter.
     */
    private void loadSets() {
        setViewModel.loadLastSets(assignmentId, sets -> {
            allSets = sets;
            updateAdapterWithGroupedSets(sets);
        });
    }

    /**
     * Aktualisiert den Adapter mit den gruppierten Sets.
     *
     * @param sets Die zu gruppierenden ExerciseSets.
     */
    private void updateAdapterWithGroupedSets(List<ExerciseSet> sets) {
        requireActivity().runOnUiThread(() ->
                adapter.setData(groupByDate(sets)));
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
            addSetToGroup(groups, set);
        }
        return groups;
    }

    /**
     * Fügt einen ExerciseSet der passenden Gruppe hinzu oder erstellt eine neue Gruppe.
     *
     * @param groups Die Liste der bestehenden Gruppen.
     * @param set Der hinzuzufügende ExerciseSet.
     */
    private void addSetToGroup(List<TrainingSetsAdapter.ExerciseSetGroup> groups, ExerciseSet set) {
        for (TrainingSetsAdapter.ExerciseSetGroup group : groups) {
            if (group.getDate().equals(set.getDate())) {
                group.getSets().add(set);
                return;
            }
        }
        createNewGroup(groups, set);
    }

    /**
     * Erstellt eine neue Gruppe für den gegebenen ExerciseSet.
     *
     * @param groups Die Liste der bestehenden Gruppen.
     * @param set Der ExerciseSet für die neue Gruppe.
     */
    private void createNewGroup(List<TrainingSetsAdapter.ExerciseSetGroup> groups, ExerciseSet set) {
        List<ExerciseSet> newGroupList = new ArrayList<>();
        newGroupList.add(set);
        groups.add(new TrainingSetsAdapter.ExerciseSetGroup(set.getDate(), newGroupList));
    }
}