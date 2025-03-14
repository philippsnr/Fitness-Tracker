package com.example.fitnesstracker.ui.training;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.viewmodel.TrainingplanViewModel;
import com.example.fitnesstracker.model.Trainingplan;

import java.util.ArrayList;

public class TrainingFragment extends Fragment {

    private TrainingplanViewModel viewModel;
    private TrainingplanAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        initViewModel();
        loadTrainingPlans();
    }

    /**
     * Initialisiert die UI-Elemente und setzt den Adapter für die RecyclerView.
     */
    private void initUI(View view) {
        TextView tvActivePlan = view.findViewById(R.id.tvActivePlan);
        RecyclerView rvTrainingPlans = view.findViewById(R.id.rvTrainingPlans);
        rvTrainingPlans.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TrainingplanAdapter(new ArrayList<>(), new TrainingplanAdapter.OnItemClickListener() {
            @Override
            public void onViewClick(int position) {
                openTrainingPlanDetails(position);
            }
            @Override
            public void onEditClick(int position) {
                showRenameDialog(adapter.getItem(position), position);
            }
            @Override
            public void onDeleteClick(int position) {
                showDeleteConfirmationDialog(adapter.getItem(position));
            }
        });
        rvTrainingPlans.setAdapter(adapter);
    }

    /**
     * Initialisiert das ViewModel.
     */
    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(TrainingplanViewModel.class);
    }

    /**
     * Lädt alle Trainingspläne und den aktiven Plan.
     */
    private void loadTrainingPlans() {
        viewModel.loadActiveTrainingplan(
                plan -> requireActivity().runOnUiThread(() -> updateActivePlan(plan)),
                error -> Log.e("Trainingplan", "Fehler beim Laden des aktiven Plans", error)
        );

        viewModel.loadAllTrainingplans(
                plans -> requireActivity().runOnUiThread(() -> adapter.updatePlans(plans)),
                error -> Log.e("Trainingplan", "Fehler beim Laden der Trainingspläne", error)
        );
    }

    /**
     * Aktualisiert die Anzeige des aktiven Trainingsplans.
     */
    private void updateActivePlan(Trainingplan plan) {
        TextView tvActivePlan = requireView().findViewById(R.id.tvActivePlan);
        tvActivePlan.setText(plan != null ? plan.getName() : "Kein aktiver Plan");
    }

    /**
     * Öffnet die Detailansicht eines Trainingsplans.
     */
    private void openTrainingPlanDetails(int position) {
        Trainingplan plan = adapter.getItem(position);
        TrainingdayFragment trainingdayFragment = TrainingdayFragment.newInstance(plan.getId());

        getParentFragmentManager().beginTransaction()
                .replace(R.id.detailContainer, trainingdayFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Zeigt einen Dialog zum Umbenennen eines Trainingsplans an.
     */
    private void showRenameDialog(Trainingplan plan, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Trainingplan umbenennen");

        final EditText input = new EditText(requireContext());
        input.setText(plan.getName());
        builder.setView(input);

        builder.setPositiveButton("Speichern", (dialog, which) -> saveNewName(plan, input, position));
        builder.setNegativeButton("Abbrechen", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Speichert den neuen Namen des Trainingsplans und aktualisiert die Ansicht.
     */
    private void saveNewName(Trainingplan plan, EditText input, int position) {
        String newName = input.getText().toString().trim();
        if (!newName.isEmpty()) {
            plan.setName(newName);
            viewModel.updateTrainingplan(plan,
                    () -> requireActivity().runOnUiThread(() -> adapter.notifyItemChanged(position)),
                    this::showError
            );
        }
    }

    /**
     * Zeigt eine Fehlermeldung an, falls das Speichern fehlschlägt.
     */
    private void showError(Exception exception) {
        requireActivity().runOnUiThread(() -> {
            Toast.makeText(requireContext(), "Fehler beim Speichern", Toast.LENGTH_SHORT).show();
            Log.e("Trainingplan", "Update-Fehler", exception);
        });
    }

    /**
     * Zeigt einen Bestätigungsdialog zum Löschen eines Trainingsplans an.
     */
    private void showDeleteConfirmationDialog(Trainingplan plan) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Löschen bestätigen")
                .setMessage("Möchten Sie diesen Trainingsplan wirklich unwiderruflich löschen?")
                .setPositiveButton("Ja", (dialog, which) -> deleteTrainingplan(plan))
                .setNegativeButton("Nein", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    /**
     * Löscht den Trainingsplan und aktualisiert die Liste.
     */
    private void deleteTrainingplan(Trainingplan plan) {
        viewModel.deleteTrainingplan(plan,
                () -> requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Trainingsplan gelöscht", Toast.LENGTH_SHORT).show();
                    reloadTrainingplans();
                }),
                this::showDeleteError
        );
    }

    /**
     * Lädt alle Trainingspläne neu und aktualisiert die Anzeige.
     */
    private void reloadTrainingplans() {
        viewModel.loadAllTrainingplans(
                plans -> requireActivity().runOnUiThread(() -> adapter.updatePlans(plans)),
                error -> Log.e("Trainingplan", "Fehler beim Laden der Trainingspläne", error)
        );
    }

    /**
     * Zeigt eine Fehlermeldung, falls das Löschen fehlschlägt.
     */
    private void showDeleteError(Exception error) {
        requireActivity().runOnUiThread(() -> {
            Toast.makeText(requireContext(), "Fehler beim Löschen des Plans", Toast.LENGTH_SHORT).show();
            Log.e("Trainingplan", "Löschfehler", error);
        });
    }
}
