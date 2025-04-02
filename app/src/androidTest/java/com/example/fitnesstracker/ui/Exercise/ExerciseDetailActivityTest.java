package com.example.fitnesstracker.ui.Exercise;

import android.content.Intent;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.ui.exercise.ExerciseDetailActivity;

@RunWith(AndroidJUnit4.class)
public class ExerciseDetailActivityTest {

    @Rule
    public ActivityTestRule<ExerciseDetailActivity> activityRule = new ActivityTestRule<>(
            ExerciseDetailActivity.class, true, false);

    @Test
    public void testExerciseDetailDisplay() {
        // Erstelle ein Intent mit den Testdaten
        Intent intent = new Intent();
        intent.putExtra("exerciseName", "Push-Up");
        intent.putExtra("exerciseInfo", "Eine grundlegende Kraftübung");
        intent.putExtra("exercisePicturePath", "push_up_image");

        // Starte die Aktivität mit dem Intent
        activityRule.launchActivity(intent);

        // Überprüfe, ob die UI-Elemente den korrekten Text anzeigen
        onView(withId(R.id.textViewExerciseDetailName)).check(matches(withText("Push-Up")));
        onView(withId(R.id.textViewExerciseDetailInfo)).check(matches(withText("Eine grundlegende Kraftübung")));
    }
}
