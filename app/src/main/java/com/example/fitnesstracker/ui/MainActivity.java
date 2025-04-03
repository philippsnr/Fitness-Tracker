package com.example.fitnesstracker.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.ui.nutrition.SearchNutritionFragment;
import com.example.fitnesstracker.ui.progression.ProgressionFragment;
import com.example.fitnesstracker.ui.exercise.ExerciseFragment;
import com.example.fitnesstracker.ui.training.TrainingPlanFragment;
import com.example.fitnesstracker.ui.onboarding.OnboardingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Main activity that serves as the entry point and navigation hub of the application.
 * Handles onboarding flow, bottom navigation, and fragment management.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting. Sets up the UI and checks for onboarding status.
     *
     * @param savedInstanceState If non-null, this activity is being re-constructed from a previous saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if onboarding should be shown
        if (shouldShowOnboarding()) {
            startOnboarding();
            return;
        }

        setupBottomNavigation();

        // Load default fragment if this is a fresh launch
        if (savedInstanceState == null) {
            loadFragment(new ProgressionFragment());
        }
    }

    /**
     * Determines whether the onboarding flow should be displayed.
     *
     * @return true if onboarding hasn't been completed, false otherwise
     */
    private boolean shouldShowOnboarding() {
        SharedPreferences prefs = getSharedPreferences("onboarding", Context.MODE_PRIVATE);
        return !prefs.getBoolean("onboarding_complete", false);
    }

    /**
     * Starts the onboarding activity and finishes the main activity after a short delay.
     */
    private void startOnboarding() {
        Intent intent = new Intent(this, OnboardingActivity.class);
        startActivity(intent);

        // Delay finish to allow onboarding activity to start properly
        new android.os.Handler().postDelayed(this::finish, 100);
    }

    /**
     * Sets up the bottom navigation bar and its click listeners.
     */
    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = getSelectedFragment(item.getItemId());
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    /**
     * Maps navigation item IDs to their corresponding fragments.
     *
     * @param itemId the ID of the selected navigation item
     * @return the Fragment corresponding to the selected item, or null if no match found
     */
    private Fragment getSelectedFragment(int itemId) {
        if (itemId == R.id.nav_progression) {
            return new ProgressionFragment();
        } else if (itemId == R.id.nav_nutrition) {
            return new SearchNutritionFragment();
        } else if (itemId == R.id.nav_training) {
            return new TrainingPlanFragment();
        } else if (itemId == R.id.nav_exercise) {
            return new ExerciseFragment();
        }
        return null;
    }

    /**
     * Loads the specified fragment into the fragment container.
     *
     * @param fragment the Fragment to be displayed
     */
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commitNow();
    }
}