package com.example.fitnesstracker.ui.training;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.fitnesstracker.model.Trainingplan;
import com.example.fitnesstracker.viewmodel.TrainingplanViewModel;

import java.util.ArrayList;
import java.util.List;

public class TrainingPlanFragment extends Fragment {

    private TrainingplanViewModel viewModel;
    private TrainingPlanAdapter adapter;
    private Trainingplan activePlan;
    private ImageView ivChangeActivePlan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainingplan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        initViewModel();
        loadTrainingPlans();
    }

    /**
     * Initialisiert die UI-Elemente und setzt den Klick-Listener für das Icon.
     */
    private void initUI(View view) {
        initViews(view);
        setupRecyclerView();
        ivChangeActivePlan.setOnClickListener(v -> showOtherTrainingplansDialog());
    }

    /**
     * Initialisiert die Views anhand der übergebenen Root-View.
     */
    private void initViews(View view) {
        TextView tvActivePlan = view.findViewById(R.id.tvActivePlan);
        RecyclerView rvTrainingPlans = view.findViewById(R.id.rvTrainingPlans);
        ivChangeActivePlan = view.findViewById(R.id.ivChangeActivePlan);
    }

    /**
     * Initialisiert das RecyclerView mit dem zugehörigen Adapter und Click-Listenern.
     */
    private void setupRecyclerView() {
        RecyclerView rvTrainingPlans = requireView().findViewById(R.id.rvTrainingPlans);
        rvTrainingPlans.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TrainingPlanAdapter(new ArrayList<>(), createItemClickListener());
        rvTrainingPlans.setAdapter(adapter);
    }

    /**
     * Erstellt und gibt den OnItemClickListener für das RecyclerView zurück.
     */
    private TrainingPlanAdapter.OnItemClickListener createItemClickListener() {
        return new TrainingPlanAdapter.OnItemClickListener() {
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

            @Override
            public void onChangeActiveClick(int position) {
                showOtherTrainingplansDialog();
            }
        };
    }

    /**
     * Zeigt einen Dialog, in dem ein inaktiver Trainingsplan als aktiv ausgewählt werden kann.
     */
    private void showOtherTrainingplansDialog() {
        List<Trainingplan> inactivePlans = getInactiveTrainingplans();
        if (inactivePlans.isEmpty()) {
            Toast.makeText(requireContext(), "Keine anderen Trainingspläne zum Aktivieren vorhanden", Toast.LENGTH_SHORT).show();
            return;
        }
        showTrainingplanSelectionDialog(inactivePlans);
    }

    /**
     * Erstellt eine Liste aller inaktiven Trainingspläne.
     */
    private List<Trainingplan> getInactiveTrainingplans() {
        List<Trainingplan> inactivePlans = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            Trainingplan plan = adapter.getItem(i);
            if (activePlan != null && plan.getId() != activePlan.getId()) {
                inactivePlans.add(plan);
            }
        }
        return inactivePlans;
    }

    /**
     * Zeigt einen Dialog zur Auswahl eines neuen aktiven Trainingsplans.
     */
    private void showTrainingplanSelectionDialog(List<Trainingplan> inactivePlans) {
        String[] planNames = new String[inactivePlans.size()];
        for (int i = 0; i < inactivePlans.size(); i++) {
            planNames[i] = inactivePlans.get(i).getName();
        }
        new AlertDialog.Builder(requireContext())
                .setTitle("Wähle einen Trainingsplan als aktiv")
                .setItems(planNames, (dialog, which) -> showConfirmationDialog(inactivePlans.get(which)))
                .show();
    }

    /**
     * Zeigt einen Bestätigungsdialog zur Aktivierung eines ausgewählten Trainingsplans.
     */
    private void showConfirmationDialog(Trainingplan selectedPlan) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Bestätigung")
                .setMessage("Möchten Sie den Trainingsplan \"" + selectedPlan.getName() + "\" als aktiv setzen?")
                .setPositiveButton("Ja", (confirmDialog, whichButton) -> activateTrainingplan(selectedPlan))
                .setNegativeButton("Nein", (confirmDialog, whichButton) -> confirmDialog.dismiss())
                .show();
    }

    /**
     * Aktiviert den ausgewählten Trainingsplan und lädt die Daten neu.
     */
    private void activateTrainingplan(Trainingplan selectedPlan) {
        viewModel.setActiveTrainingplan(selectedPlan.getId(),
                () -> requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Aktiver Trainingsplan geändert", Toast.LENGTH_SHORT).show();
                    loadTrainingPlans();
                }),
                error -> requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Fehler beim Aktivieren des Trainingsplans", Toast.LENGTH_SHORT).show()
                )
        );
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
        activePlan = plan;
        TextView tvActivePlan = requireView().findViewById(R.id.tvActivePlan);
        tvActivePlan.setText(plan != null ? plan.getName() : "Kein aktiver Plan");
    }

    /**
     * Öffnet die Detailansicht eines Trainingsplans.
     * Hier wird das TrainingDayFragment in den fragment_container geladen,
     * sodass die Bottom Navigation sichtbar bleibt.
     */
    private void openTrainingPlanDetails(int position) {
        Trainingplan plan = adapter.getItem(position);
        Log.d("TrainingPlanFragment", "Klick auf Plan: " + plan.getName());

        TrainingDayFragment detailsFragment = TrainingDayFragment.newInstance(plan.getId(), plan.getName());
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
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
                    () -> requireActivity().runOnUiThread(() -> {
                        adapter.notifyItemChanged(position);
                        if (activePlan != null && activePlan.getId() == plan.getId()) {
                            activePlan.setName(newName);
                            TextView tvActivePlan = requireView().findViewById(R.id.tvActivePlan);
                            tvActivePlan.setText(newName);
                        }
                    }),
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
