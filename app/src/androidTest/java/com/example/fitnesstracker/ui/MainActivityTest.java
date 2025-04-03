package com.example.fitnesstracker.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.ui.exercise.ExerciseFragment;
import com.example.fitnesstracker.ui.nutrition.SearchNutritionFragment;
import com.example.fitnesstracker.ui.onboarding.OnboardingActivity;
import com.example.fitnesstracker.ui.progression.ProgressionFragment;
import com.example.fitnesstracker.ui.training.TrainingPlanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.Fragment;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences prefs = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }

    @After
    public void tearDown() {

        SharedPreferences prefs = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }

    @Test
    public void testOnboardingLaunched() {
        SharedPreferences prefs = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("onboarding_complete", false).commit();

        Intents.init();


        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            intended(hasComponent(OnboardingActivity.class.getName()));
        } finally {
            Intents.release();
        }
    }

    @Test
    public void testDefaultFragmentAndBottomNavigationSwitch() {
        SharedPreferences prefs = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("onboarding_complete", true).commit();

        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {

            scenario.onActivity(activity -> {
                Fragment currentFragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                assertTrue("Expected instance of ProgressionFragment", currentFragment instanceof ProgressionFragment);
            });

            scenario.onActivity(activity -> {
                BottomNavigationView bottomNav = activity.findViewById(R.id.bottom_navigation);

                bottomNav.setSelectedItemId(R.id.nav_nutrition);
                activity.getSupportFragmentManager().executePendingTransactions();
                Fragment currentFragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                assertTrue("Expected instance of NutritionFragment", currentFragment instanceof SearchNutritionFragment);

                bottomNav.setSelectedItemId(R.id.nav_training);
                activity.getSupportFragmentManager().executePendingTransactions();
                currentFragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                assertTrue("Expected instance of TrainingPlanFragment", currentFragment instanceof TrainingPlanFragment);

                bottomNav.setSelectedItemId(R.id.nav_exercise);
                activity.getSupportFragmentManager().executePendingTransactions();
                currentFragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                assertTrue("Expected instance of ExerciseFragment", currentFragment instanceof ExerciseFragment);
            });
        }
    }
}
