package com.example.fitnesstracker.ui.exercise;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fitnesstracker.R;

public class ExerciseDetailActivity extends AppCompatActivity {
    /**
     * Called when the activity is starting. This method initializes the activity's UI and loads exercise details.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied.
     */
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

    /**
     * Retrieves exercise details from the intent and sets the provided TextViews accordingly.
     *
     * @param textViewName The TextView for displaying the exercise name.
     * @param textViewInfo The TextView for displaying the exercise info.
     * @return The exercise picture name retrieved from the intent extras.
     */
    protected String retrieveData(TextView textViewName, TextView textViewInfo) {
        String exerciseName = getIntent().getStringExtra("exerciseName");
        String exerciseInfo = getIntent().getStringExtra("exerciseInfo");
        String exercisePictureName = getIntent().getStringExtra("exercisePicturePath");

        textViewName.setText(exerciseName);
        textViewInfo.setText(exerciseInfo);
        return exercisePictureName;
    }

    /**
     * Loads the exercise image into the provided ImageView based on the picture name.
     * If the picture name is invalid or not found, a placeholder image is used instead.
     *
     * @param exercisePictureName The name of the exercise image resource.
     * @param imageViewExercise   The ImageView in which the image will be displayed.
     */
    protected void loadImage(String exercisePictureName, ImageView imageViewExercise) {
        if (exercisePictureName != null && !exercisePictureName.isEmpty()) {
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
