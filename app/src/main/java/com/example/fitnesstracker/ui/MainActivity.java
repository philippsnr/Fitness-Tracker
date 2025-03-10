package com.example.fitnesstracker.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

        initBottomNavigation();

        // Standardmäßig das Home-Fragment anzeigen
        if (savedInstanceState == null) {
            loadFragment(new ProgressionFragment());
        }
    }

    /**
     * Initialisiert die Bottom Navigation Bar und setzt den Listener.
     */
    private void initBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    /**
     * Behandelt die Auswahl eines Menü-Items in der Bottom Navigation.
     */
    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = getFragmentForMenuItem(item.getItemId());

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
            return true;
        }
        return false;
    }

    /**
     * Gibt das entsprechende Fragment für das ausgewählte Menü-Item zurück.
     */
    @Nullable
    private Fragment getFragmentForMenuItem(int itemId) {
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

    /**
     * Methode zum Laden eines Fragments.
     */
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment existingFragment = fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName());

        if (existingFragment == null) {
            fragmentTransaction.add(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        }

        for (Fragment frag : fragmentManager.getFragments()) {
            if (frag == existingFragment) {
                fragmentTransaction.show(frag);
            } else {
                fragmentTransaction.hide(frag);
            }
        }

        fragmentTransaction.commit();
    }
}