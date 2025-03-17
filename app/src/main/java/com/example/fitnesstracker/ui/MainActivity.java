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
import com.example.fitnesstracker.ui.nutrition.NutritionFragment;
import com.example.fitnesstracker.ui.progress.ProgressionFragment;
import com.example.fitnesstracker.ui.exercise.ExerciseFragment;
import com.example.fitnesstracker.ui.training.TrainingFragment;
import com.example.fitnesstracker.ui.onboarding.OnboardingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (shouldShowOnboarding()) {
            startOnboarding();
            return;
        }

        setupBottomNavigation();

        if (savedInstanceState == null) {
            loadFragment(new ProgressionFragment());
        }
    }

    private boolean shouldShowOnboarding() {
        SharedPreferences prefs = getSharedPreferences("onboarding", Context.MODE_PRIVATE);
        return !prefs.getBoolean("onboarding_complete", false);
    }

    private void startOnboarding() {
        Intent intent = new Intent(this, OnboardingActivity.class);
        startActivity(intent);

        new android.os.Handler().postDelayed(this::finish, 100);
    }

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

    private Fragment getSelectedFragment(int itemId) {
        if (itemId == R.id.nav_progression) {
            return new ProgressionFragment();
        } else if (itemId == R.id.nav_nutrition) {
            return new NutritionFragment();
        } else if (itemId == R.id.nav_training) {
            return new TrainingFragment();
        } else if (itemId == R.id.nav_exercise) {
            return new ExerciseFragment();
        }
        return null;
    }




    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
