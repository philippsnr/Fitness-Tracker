package com.example.fitnesstracker.ui.training;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.model.TrainingdayExerciseAssignment;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter für die Anzeige von Übungen in einer Trainingseinheit.
 * Verwaltet die Darstellung und Interaktion mit den Übungselementen.
 */
public class TrainingExerciseAdapter extends RecyclerView.Adapter<TrainingExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exercises = new ArrayList<>();
    private List<TrainingdayExerciseAssignment> assignments = new ArrayList<>();
    private final OnItemClickListener listener;

    /**
     * Interface für Klick-Ereignisse auf Adapter-Elemente.
     */
    public interface OnItemClickListener {
        /**
         * Wird aufgerufen, wenn auf eine Übungskarte geklickt wird.
         * @param position Position des geklickten Elements in der Liste
         */
        void onCardClick(int position);

        /**
         * Wird aufgerufen, wenn auf den Löschen-Button geklickt wird.
         * @param position Position des geklickten Elements in der Liste
         */
        void onDeleteClick(int position);
    }

    /**
     * Konstruktor für den Adapter.
     * @param listener Listener für Klick-Ereignisse
     */
    public TrainingExerciseAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Erstellt einen neuen ViewHolder für Übungselemente.
     * @param parent Die Eltern-ViewGroup
     * @param viewType Der Typ der View
     * @return Ein neuer ExerciseViewHolder
     */
    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trainingexercise, parent, false);
        return new ExerciseViewHolder(view, listener);
    }

    /**
     * Bindet Daten an die View an der angegebenen Position.
     * @param holder Der ViewHolder, der die Daten halten soll
     * @param position Position des Elements in der Liste
     */
    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        if (position < exercises.size()) {
            Exercise exercise = exercises.get(position);
            holder.bind(exercise);

            // Setzt Klick-Listener mit Positionsvalidierung
            holder.itemView.setOnClickListener(v -> {
                if (listener != null && position < assignments.size()) {
                    listener.onCardClick(position);
                }
            });

            holder.deleteButton.setOnClickListener(v -> {
                if (listener != null && position < assignments.size()) {
                    listener.onDeleteClick(position);
                }
            });
        }
    }

    /**
     * Gibt die Anzahl der Übungen in der Liste zurück.
     * @return Anzahl der Übungen
     */
    @Override
    public int getItemCount() {
        return exercises.size();
    }

    /**
     * Setzt die Liste der Übungen und aktualisiert die Anzeige.
     * @param exercises Liste der Übungen
     */
    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises != null ? exercises : new ArrayList<>();
        notifyDataSetChanged();
    }

    /**
     * Setzt die Liste der Übungszuordnungen und aktualisiert die Anzeige.
     * @param assignments Liste der Übungszuordnungen
     */
    public void setAssignments(List<TrainingdayExerciseAssignment> assignments) {
        this.assignments = assignments != null ? assignments : new ArrayList<>();
        notifyDataSetChanged();
    }

    /**
     * ViewHolder für Übungselemente.
     */
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewExerciseName;
        public final ImageView deleteButton;
        private final CardView cardView;

        /**
         * Konstruktor für den ViewHolder.
         * @param itemView Die Item-View
         * @param listener Listener für Klick-Ereignisse
         */
        public ExerciseViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewExercise);
            textViewExerciseName = itemView.findViewById(R.id.textViewTrainingExerciseName);
            deleteButton = itemView.findViewById(R.id.ivDeleteExercise);
        }

        /**
         * Bindet eine Übung an die View.
         * @param exercise Die Übung, die angezeigt werden soll
         */
        public void bind(Exercise exercise) {
            textViewExerciseName.setText(exercise.getName());
        }
    }

    /**
     * Gibt die ID der Übungszuordnung an der angegebenen Position zurück.
     * @param position Position in der Liste
     * @return ID der Übungszuordnung oder -1 bei ungültiger Position
     */
    public int getAssignmentIdAtPosition(int position) {
        if (position >= 0 && position < assignments.size()) {
            return assignments.get(position).getId();
        }
        return -1;
    }

    /**
     * Gibt die ID der Übung an der angegebenen Position zurück.
     * @param position Position in der Liste
     * @return ID der Übung oder -1 bei ungültiger Position
     */
    public int getExerciseIdAtPosition(int position) {
        if (position >= 0 && position < exercises.size()) {
            return exercises.get(position).getId();
        }
        return -1;
    }
}