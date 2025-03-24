package com.example.fitnesstracker.ui.training;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.fitnesstracker.model.Trainingday;
import com.example.fitnesstracker.viewmodel.TrainingdayViewModel;
import java.util.ArrayList;
import java.util.List;

public class TrainingDayFragment extends Fragment {

    private static final String ARG_TRAININGPLAN_ID = "trainingplan_id";
    private static final String ARG_TRAININGPLAN_NAME = "trainingplan_name";

    private int trainingplanId;
    private String trainingplanName;
    private TrainingdayViewModel viewModel;
    private RecyclerView recyclerView;
    private TrainingDayAdapter adapter;
    private TextView tvTrainingPlanTitle;
    private CardView cardAddTrainingday;

    /**
     * Erzeugt eine neue Instanz des Fragments.
     */
    public static TrainingDayFragment newInstance(int trainingplanId, String trainingplanName) {
        TrainingDayFragment fragment = new TrainingDayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRAININGPLAN_ID, trainingplanId);
        args.putString(ARG_TRAININGPLAN_NAME, trainingplanName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trainingplanId = getArguments().getInt(ARG_TRAININGPLAN_ID);
            trainingplanName = getArguments().getString(ARG_TRAININGPLAN_NAME);
        }
        viewModel = new ViewModelProvider(this).get(TrainingdayViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainingday, container, false);
        tvTrainingPlanTitle = view.findViewById(R.id.tvTrainingdayTitle);
        recyclerView = view.findViewById(R.id.recyclerViewTrainingday);
        cardAddTrainingday = view.findViewById(R.id.cardAddTrainingday);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTrainingPlanTitle.setText(trainingplanName);
        setupRecyclerView();
        loadTrainingdays();
        cardAddTrainingday.setOnClickListener(v -> showAddTrainingdayDialog());
    }

    /** Initialisiert das RecyclerView mit Adapter und LayoutManager. */
    private void setupRecyclerView() {
        adapter = new TrainingDayAdapter(new ArrayList<>(), createItemClickListener());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    /** Erstellt den OnItemClickListener für den Adapter. */
    private TrainingDayAdapter.OnItemClickListener createItemClickListener() {
        return new TrainingDayAdapter.OnItemClickListener() {
            @Override
            public void onViewClick(int position) { openTrainingdayDetails(position); }
            @Override
            public void onEditClick(int position) { showRenameDialog(adapter.getItem(position), position); }
            @Override
            public void onDeleteClick(int position) { showDeleteConfirmationDialog(adapter.getItem(position)); }
        };
    }

    /** Öffnet die Detailansicht des ausgewählten Trainingstags. */
    private void openTrainingdayDetails(int position) {
        Trainingday day = adapter.getItem(position);
        Log.d("TrainingDayFragment", "Clicked: " + day.getName());
        TrainingExerciseFragment fragment = TrainingExerciseFragment.newInstance(day.getId(), day.getName());
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    /** Zeigt einen Dialog zum Umbenennen eines Trainingstags. */
    private void showRenameDialog(Trainingday day, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Trainingstag umbenennen");
        EditText input = new EditText(requireContext());
        input.setText(day.getName());
        builder.setView(input);
        builder.setPositiveButton("Speichern", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) updateTrainingdayName(day, newName, position);
        });
        builder.setNegativeButton("Abbrechen", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /** Aktualisiert den Namen eines Trainingstags und aktualisiert den Adapter. */
    private void updateTrainingdayName(Trainingday day, String newName, int position) {
        day.setName(newName);
        viewModel.updateTrainingday(day, new TrainingdayViewModel.OnOperationCompleteListener() {
            @Override
            public void onComplete() {
                requireActivity().runOnUiThread(() -> adapter.notifyItemChanged(position));
            }
            @Override
            public void onError(Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Fehler beim Aktualisieren", Toast.LENGTH_SHORT).show());
            }
        });
    }

    /** Zeigt einen Bestätigungsdialog zum Löschen eines Trainingstags an. */
    private void showDeleteConfirmationDialog(Trainingday day) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Löschen bestätigen")
                .setMessage("Möchten Sie diesen Trainingstag wirklich löschen?")
                .setPositiveButton("Ja", (dialog, which) -> deleteTrainingday(day))
                .setNegativeButton("Nein", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /** Löscht den Trainingstag und lädt die Liste neu. */
    private void deleteTrainingday(Trainingday day) {
        viewModel.deleteTrainingday(day, new TrainingdayViewModel.OnOperationCompleteListener() {
            @Override
            public void onComplete() {
                requireActivity().runOnUiThread(() -> loadTrainingdays());
            }
            @Override
            public void onError(Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Fehler beim Löschen", Toast.LENGTH_SHORT).show());
            }
        });
    }

    /** Lädt die Trainingstage des aktuellen Plans. */
    private void loadTrainingdays() {
        viewModel.getTrainingdaysForPlan(trainingplanId, trainingdays ->
                requireActivity().runOnUiThread(() -> adapter.updateData(trainingdays)));
    }

    /** Zeigt einen Dialog an, um einen neuen Trainingstag hinzuzufügen. */
    private void showAddTrainingdayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Neuen Trainingstag hinzufügen");
        EditText input = new EditText(requireContext());
        input.setHint("Name des Trainingstags");
        builder.setView(input);
        builder.setPositiveButton("Hinzufügen", (dialog, which) -> {
            String name = input.getText().toString().trim();
            if (!name.isEmpty()) addNewTrainingday(name);
        });
        builder.setNegativeButton("Abbrechen", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /** Erstellt einen neuen Trainingstag und lädt die Liste neu. */
    private void addNewTrainingday(String name) {
        Trainingday newDay = new Trainingday(0, name, trainingplanId);
        viewModel.createTrainingday(newDay, new TrainingdayViewModel.OnOperationCompleteListener() {
            @Override
            public void onComplete() {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Trainingstag hinzugefügt", Toast.LENGTH_SHORT).show();
                    loadTrainingdays();
                });
            }
            @Override
            public void onError(Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Fehler beim Hinzufügen", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
