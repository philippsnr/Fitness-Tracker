package com.example.fitnesstracker.ui.exercise;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.fitnesstracker.R;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExerciseDetailActivityTest {

    @Test
    public void testExerciseDetailActivityDisplaysData() {
        // Erstelle ein Intent mit den nötigen Extras
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                ExerciseDetailActivity.class);
        intent.putExtra("exerciseName", "Test Exercise");
        intent.putExtra("exerciseInfo", "This is a test exercise.");
        // In der Datenbank ist nur der Bildname gespeichert – hier wird "placeholder_image" verwendet.
        intent.putExtra("exercisePicturePath", "placeholder_image");

        try (ActivityScenario<ExerciseDetailActivity> scenario = ActivityScenario.launch(intent)) {
            // Überprüfe, ob die TextViews den richtigen Text anzeigen.
            onView(withId(R.id.textViewExerciseDetailName))
                    .check(matches(withText("Test Exercise")));
            onView(withId(R.id.textViewExerciseDetailInfo))
                    .check(matches(withText("This is a test exercise.")));
            // Überprüfe, ob das ImageView angezeigt wird.
            onView(withId(R.id.imageViewExercise))
                    .check(matches(isDisplayed()));
        }
    }
}