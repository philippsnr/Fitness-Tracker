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

        // Prüfen, ob das Onboarding bereits abgeschlossen wurde
        SharedPreferences prefs = getSharedPreferences("onboarding", Context.MODE_PRIVATE);
        boolean onboardingComplete = prefs.getBoolean("onboarding_complete", false);

        if (!onboardingComplete) {
            // Falls das Onboarding noch nicht abgeschlossen ist, starte es und beende MainActivity
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);new android.os.Handler().postDelayed(() -> {
                finish();
            }, 100);
            return;
        }

        // Navigation nach dem Onboarding
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_progression) {
                selectedFragment = new ProgressionFragment();
            } else if (itemId == R.id.nav_nutrition) {
                selectedFragment = new NutritionFragment();
            } else if (itemId == R.id.nav_training) {
                selectedFragment = new TrainingFragment();
            } else if (itemId == R.id.nav_exercise) {
                selectedFragment = new ExerciseFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }

            return true;
        });

        if (savedInstanceState == null) {
            loadFragment(new ProgressionFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
