package com.example.fitnesstracker.ui;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom Navigation Bar initialisieren
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Listener für die Bottom Navigation Bar mit Lambda-Ausdruck
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Verwende if-else anstelle von switch
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

            // Fragment austauschen
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }

            return true;
        });

        // Standardmäßig das Home-Fragment anzeigen
        if (savedInstanceState == null) {
            loadFragment(new ProgressionFragment());
        }
    }

    // Methode zum Laden eines Fragments
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}