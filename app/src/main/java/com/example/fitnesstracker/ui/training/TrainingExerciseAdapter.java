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
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter für die Anzeige von Übungen in einem Trainingstag.
 */
public class TrainingExerciseAdapter extends RecyclerView.Adapter<TrainingExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exercises = new ArrayList<>();
    private OnItemClickListener listener;

    /**
     * Schnittstelle für Klick-Ereignisse.
     */
    public interface OnItemClickListener {
        void onCardClick(int position);
        void onDeleteClick(int position);
    }

    /**
     * Konstruktor.
     *
     * @param listener Listener für Klick-Ereignisse.
     */
    public TrainingExerciseAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trainingexercise, parent, false);
        return new ExerciseViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.bind(exercises.get(position));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    /**
     * Aktualisiert die Liste der Übungen.
     *
     * @param newExercises Neue Übungsliste.
     */
    public void setExercises(List<Exercise> newExercises) {
        this.exercises = newExercises;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder für eine Übungskarte.
     */
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewExerciseName;
        private ImageView ivDelete;
        private CardView cardView;

        /**
         * Konstruktor.
         *
         * @param itemView Die Item-View.
         * @param listener Listener für Klick-Ereignisse.
         */
        public ExerciseViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            cardView = (CardView) itemView;
            textViewExerciseName = itemView.findViewById(R.id.textViewTrainingExerciseName);
            ivDelete = itemView.findViewById(R.id.ivDeleteExercise);
            cardView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                    listener.onCardClick(getAdapterPosition());
            });
            ivDelete.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                    listener.onDeleteClick(getAdapterPosition());
            });
        }

        /**
         * Bindet die Übung an die Views.
         *
         * @param exercise Das Übungsobjekt.
         */
        public void bind(Exercise exercise) {
            textViewExerciseName.setText(exercise.getName());
        }
    }
}
