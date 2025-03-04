package com.example.fitnesstracker.ui.training;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.viewmodel.TrainingplanViewModel;
import com.example.fitnesstracker.model.Trainingplan;

import java.util.ArrayList;

public class TrainingFragment extends Fragment {

    private TrainingplanViewModel viewModel;
    private TrainingplanAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvActivePlan = view.findViewById(R.id.tvActivePlan);
        RecyclerView rvTrainingPlans = view.findViewById(R.id.rvTrainingPlans);
        rvTrainingPlans.setLayoutManager(new LinearLayoutManager(requireContext()));

        // ViewModel initialisieren
        viewModel = new ViewModelProvider(requireActivity()).get(TrainingplanViewModel.class);

        // Adapter initialisieren und den Listener setzen
        adapter = new TrainingplanAdapter(new ArrayList<>(), new TrainingplanAdapter.OnItemClickListener() {
            @Override
            public void onViewClick(int position) {
                // Logik für "Ansehen" einfügen
                Trainingplan plan = adapter.getItem(position);
                System.out.println("Details anzeigen für: " + plan.getName());
            }

            @Override
            public void onEditClick(int position) {
                Trainingplan plan = adapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext()); // Context aus dem Fragment holen
                builder.setTitle("Trainingplan umbenennen");

                final EditText input = new EditText(requireContext()); // Context für EditText
                input.setText(plan.getName());
                builder.setView(input);

                builder.setPositiveButton("Speichern", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        plan.setName(newName);

                        viewModel.updateTrainingplan(plan,
                                () -> {
                                    requireActivity().runOnUiThread(() -> {
                                        adapter.notifyItemChanged(position);
                                    });
                                },
                                exception -> {
                                    requireActivity().runOnUiThread(() -> {
                                        Toast.makeText(requireContext(), "Fehler beim Speichern", Toast.LENGTH_SHORT).show();
                                        Log.e("Trainingplan", "Update-Fehler", exception);
                                    });
                                }
                        );
                    }
                });

                builder.setNegativeButton("Abbrechen", (dialog, which) -> dialog.cancel());

                builder.show();
            }



            @Override
            public void onDeleteClick(int position) {
                // Zeige einen Bestätigungsdialog
                new AlertDialog.Builder(requireContext())
                        .setTitle("Löschen bestätigen")
                        .setMessage("Möchten Sie diesen Trainingsplan wirklich unwiderruflich löschen?")
                        .setPositiveButton("Ja", (dialog, which) -> {
                            // Der Benutzer hat „Ja“ gewählt – führe die Löschaktion aus
                            Trainingplan plan = adapter.getItem(position);

                            // Aufruf der ViewModel-Methode zum Löschen des Trainingsplans
                            viewModel.deleteTrainingplan(plan,
                                    () -> requireActivity().runOnUiThread(() -> {
                                        Toast.makeText(requireContext(), "Trainingsplan gelöscht", Toast.LENGTH_SHORT).show();
                                        viewModel.loadAllTrainingplans(
                                                plans -> requireActivity().runOnUiThread(() -> adapter.updatePlans(plans)),
                                                error -> System.err.println("Fehler beim Laden der Trainingspläne: " + error.getMessage())
                                        );
                                    }),
                                    error -> requireActivity().runOnUiThread(() -> {
                                        Toast.makeText(requireContext(), "Fehler beim Löschen des Plans", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();
                                    })
                            );
                        })
                        .setNegativeButton("Nein", (dialog, which) -> {
                            // Der Benutzer hat „Nein“ gewählt – schließe einfach den Dialog
                            dialog.dismiss();
                        })
                        .setCancelable(false) // Verhindert, dass der Dialog einfach geschlossen wird
                        .show();
            }
        });

        rvTrainingPlans.setAdapter(adapter);

        // Aktiven Trainingsplan laden
        viewModel.loadActiveTrainingplan(
                plan -> requireActivity().runOnUiThread(() -> {
                    if (plan != null) {
                        tvActivePlan.setText(plan.getName());
                    } else {
                        tvActivePlan.setText("Kein aktiver Plan");
                    }
                }),
                error -> System.err.println("Fehler beim Laden des aktiven Plans: " + error.getMessage())
        );

        // Liste aller Trainingspläne laden
        viewModel.loadAllTrainingplans(
                plans -> requireActivity().runOnUiThread(() -> adapter.updatePlans(plans)),
                error -> System.err.println("Fehler beim Laden der Trainingspläne: " + error.getMessage())
        );
    }
}
