package com.example.fitnesstracker.ui.exercise;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.Exercise;
import java.util.ArrayList;
import java.util.List;
import androidx.core.content.ContextCompat;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises = new ArrayList<>();
    private Context context;

    public ExerciseAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.nameTextView.setText(exercise.getName());
        holder.difficultyTextView.setText("Difficulty: " + exercise.getDifficulty());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExerciseDetailActivity.class);
            intent.putExtra("exerciseName", exercise.getName());
            intent.putExtra("exerciseInfo", exercise.getInfo());
            intent.putExtra("exercisePicturePath", exercise.getPicturePath());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, difficultyTextView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setPadding(50, 30, 50, 30);
            itemView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.rounded_border_primary));
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.setMargins(0, 5, 0, 5);
                itemView.setLayoutParams(layoutParams);
            }

            nameTextView = itemView.findViewById(R.id.textViewExerciseName);
            nameTextView.setTextColor(Color.WHITE);
            difficultyTextView = itemView.findViewById(R.id.textViewExerciseDifficulty);
            difficultyTextView.setTextColor(Color.WHITE);
        }
    }
}
