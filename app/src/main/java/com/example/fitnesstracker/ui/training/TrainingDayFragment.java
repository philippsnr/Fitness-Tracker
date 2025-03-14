package com.example.fitnesstracker.ui.training;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    /**
     * Erstellt eine neue Instanz des Fragments.
     *
     * @param trainingplanId   Die ID des Trainingsplans.
     * @param trainingplanName Der Name des Trainingsplans.
     * @return Eine neue Instanz von TrainingDayFragment.
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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTrainingPlanTitle.setText(trainingplanName);
        setupRecyclerView();
        loadTrainingdays();
    }

    /**
     * Initialisiert das RecyclerView mit dem Adapter und dem LayoutManager.
     */
    private void setupRecyclerView() {
        adapter = new TrainingDayAdapter(new ArrayList<>(), createItemClickListener());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Erstellt den OnItemClickListener für den Adapter.
     *
     * @return Der OnItemClickListener.
     */
    private TrainingDayAdapter.OnItemClickListener createItemClickListener() {
        return new TrainingDayAdapter.OnItemClickListener() {
            @Override
            public void onViewClick(int position) {
                openTrainingdayDetails(position);
            }

            @Override
            public void onEditClick(int position) {
                showRenameDialog(adapter.getItem(position), position);
            }

            @Override
            public void onDeleteClick(int position) {
                showDeleteConfirmationDialog(adapter.getItem(position));
            }
        };
    }

    /**
     * Öffnet die Detailansicht für einen Trainingstag.
     *
     * @param position Die Position des angeklickten Trainingstags.
     */
    private void openTrainingdayDetails(int position) {
        Trainingday trainingday = adapter.getItem(position);
    }

    /**
     * Zeigt einen Dialog zum Umbenennen eines Trainingstags an.
     *
     * @param trainingday Der Trainingstag, der umbenannt werden soll.
     * @param position    Die Position des Trainingstags in der Liste.
     */
    private void showRenameDialog(Trainingday trainingday, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Trainingstag umbenennen");

        final EditText input = new EditText(requireContext());
        input.setText(trainingday.getName());
        builder.setView(input);

        builder.setPositiveButton("Speichern", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                updateTrainingdayName(trainingday, newName, position);
            }
        });
        builder.setNegativeButton("Abbrechen", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Aktualisiert den Namen eines Trainingstags.
     *
     * @param trainingday Der Trainingstag, dessen Name aktualisiert werden soll.
     * @param newName     Der neue Name des Trainingstags.
     * @param position    Die Position des Trainingstags in der Liste.
     */
    private void updateTrainingdayName(Trainingday trainingday, String newName, int position) {
        trainingday.setName(newName);
        viewModel.updateTrainingday(trainingday, new TrainingdayViewModel.OnOperationCompleteListener() {
            @Override
            public void onComplete() {
                requireActivity().runOnUiThread(() -> {
                    adapter.notifyItemChanged(position);
                });
            }

            @Override
            public void onError(Exception exception) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Fehler beim Aktualisieren", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * Zeigt einen Bestätigungsdialog zum Löschen eines Trainingstags an.
     *
     * @param trainingday Der Trainingstag, der gelöscht werden soll.
     */
    private void showDeleteConfirmationDialog(Trainingday trainingday) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Löschen bestätigen")
                .setMessage("Möchten Sie diesen Trainingstag wirklich löschen?")
                .setPositiveButton("Ja", (dialog, which) -> deleteTrainingday(trainingday))
                .setNegativeButton("Nein", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Löscht einen Trainingstag.
     *
     * @param trainingday Der Trainingstag, der gelöscht werden soll.
     */
    private void deleteTrainingday(Trainingday trainingday) {
        viewModel.deleteTrainingday(trainingday, new TrainingdayViewModel.OnOperationCompleteListener() {
            @Override
            public void onComplete() {
                requireActivity().runOnUiThread(() -> {
                    loadTrainingdays();
                });
            }

            @Override
            public void onError(Exception exception) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Fehler beim Löschen", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * Lädt die Trainingstage für den aktuellen Trainingsplan.
     */
    private void loadTrainingdays() {
        viewModel.getTrainingdaysForPlan(trainingplanId, new TrainingdayViewModel.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Trainingday> trainingdays) {
                requireActivity().runOnUiThread(() -> adapter.updateData(trainingdays));
            }
        });
    }
}