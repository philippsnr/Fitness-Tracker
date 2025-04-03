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

/**
 * Adapter class for managing and displaying a list of exercises in a RecyclerView.
 */
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises = new ArrayList<>();
    private final Context context;

    /**
     * Constructor to initialize the adapter with a given context.
     * @param context The context where the adapter is being used.
     */
    public ExerciseAdapter(Context context) {
        this.context = context;
    }

    /**
     * Creates and returns a new ViewHolder instance for an exercise item.
     * @param parent The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new ExerciseViewHolder instance.
     */
    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    /**
     * Binds data to a ViewHolder at a specific position.
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.nameTextView.setText(exercise.getName());

        String difficultyText = context.getString(R.string.e_difficulty_label, exercise.getDifficulty());
        holder.difficultyTextView.setText(difficultyText);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExerciseDetailActivity.class);
            intent.putExtra("exerciseName", exercise.getName());
            intent.putExtra("exerciseInfo", exercise.getInfo());
            intent.putExtra("exercisePicturePath", exercise.getPicturePath());
            context.startActivity(intent);
        });
    }


    /**
     * Returns the total number of exercises in the list.
     * @return The number of exercises.
     */
    @Override
    public int getItemCount() {
        return exercises.size();
    }

    /**
     * Updates the list of exercises and notifies the adapter of data changes.
     * @param exercises The new list of exercises.
     */
    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for holding and managing individual exercise item views.
     */
    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, difficultyTextView;

        /**
         * Constructor to initialize the views for an exercise item.
         * @param itemView The item view to be managed by the ViewHolder.
         */
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