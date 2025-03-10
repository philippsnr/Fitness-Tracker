package com.example.fitnesstracker.ui.exercise;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fitnesstracker.R;

public class ExerciseDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        TextView textViewName = findViewById(R.id.textViewExerciseDetailName);
        TextView textViewInfo = findViewById(R.id.textViewExerciseDetailInfo);
        ImageView imageViewExercise = findViewById(R.id.imageViewExercise);

        String exercisePictureName = retrieveData(textViewName, textViewInfo);
        loadImage(exercisePictureName, imageViewExercise);
    }

    protected String retrieveData(TextView textViewName, TextView textViewInfo) {
        String exerciseName = getIntent().getStringExtra("exerciseName");
        String exerciseInfo = getIntent().getStringExtra("exerciseInfo");
        // In der Datenbank ist jetzt nur der Bildname gespeichert
        String exercisePictureName = getIntent().getStringExtra("exercisePicturePath");

        textViewName.setText(exerciseName);
        textViewInfo.setText(exerciseInfo);
        return exercisePictureName;
    }

    protected void loadImage(String exercisePictureName, ImageView imageViewExercise) {
        if (exercisePictureName != null && !exercisePictureName.isEmpty()) {
            // Lade die Bildressource anhand des Namens
            int resId = getResources().getIdentifier(exercisePictureName, "drawable", getPackageName());
            if (resId != 0) {
                imageViewExercise.setImageResource(resId);
            } else {
                imageViewExercise.setImageResource(R.drawable.placeholder_image);
            }
        } else {
            imageViewExercise.setImageResource(R.drawable.placeholder_image);
        }
    }
}
