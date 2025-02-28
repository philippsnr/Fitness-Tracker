package com.example.fitnesstracker.ui.exercise;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fitnesstracker.R;
import java.io.File;

public class ExerciseDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        TextView textViewName = findViewById(R.id.textViewExerciseDetailName);
        TextView textViewInfo = findViewById(R.id.textViewExerciseDetailInfo);
        ImageView imageViewExercise = findViewById(R.id.imageViewExercise);

        // Retrieve data from Intent extras
        String exerciseName = getIntent().getStringExtra("exerciseName");
        String exerciseInfo = getIntent().getStringExtra("exerciseInfo");
        String exercisePicturePath = getIntent().getStringExtra("exercisePicturePath");

        textViewName.setText(exerciseName);
        textViewInfo.setText(exerciseInfo);

        // Load image from file path (assumes local file path is provided)
        loadImage(exercisePicturePath, imageViewExercise);
    }

    protected void loadImage(String exercisePicturePath, ImageView imageViewExercise) {
        if (exercisePicturePath != null && !exercisePicturePath.isEmpty()) {
            File imgFile = new File(exercisePicturePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageViewExercise.setImageBitmap(bitmap);
            } else {
                imageViewExercise.setImageResource(R.drawable.placeholder_image);
            }
        } else {
            imageViewExercise.setImageResource(R.drawable.placeholder_image);
        }
    }
}
