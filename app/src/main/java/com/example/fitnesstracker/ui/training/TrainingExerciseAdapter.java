package com.example.fitnesstracker.ui.training;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.Exercise;
import java.util.ArrayList;
import java.util.List;

public class TrainingExerciseAdapter extends RecyclerView.Adapter<TrainingExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises = new ArrayList<>();

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trainingexercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.textViewExerciseName.setText(exercise.getName());
        // Hier kannst du weitere Bindings vornehmen (z. B. Icons mit ClickListener versehen)
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewExerciseName;
        ImageView ivViewExercise;
        ImageView ivChangeExercise;
        ImageView ivDeleteExercise;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewExerciseName = itemView.findViewById(R.id.textViewTrainingExerciseName);
            ivViewExercise = itemView.findViewById(R.id.ivViewExercise);
            ivChangeExercise = itemView.findViewById(R.id.ivChangeExercise);
            ivDeleteExercise = itemView.findViewById(R.id.ivDeleteExercise);
        }
    }
}
