package com.example.fitnesstracker.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.View;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.ui.nutrition.NutritionFragment;
import com.example.fitnesstracker.ui.progress.ProgressionFragment;
import com.example.fitnesstracker.ui.exercise.ExerciseFragment;
import com.example.fitnesstracker.ui.training.TrainingplanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final long BACKSTACK_POP_DELAY = 50; // Millisekunden

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBottomNavigation();

        // Standardmäßig ein Basisfragment anzeigen (hier z. B. ProgressionFragment)
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
     * Dabei wird der Back Stack zuerst geleert, und nach einem kurzen Delay
     * (um sicherzustellen, dass alle Transaktionen abgeschlossen sind) das
     * Basisfragment geladen.
     */
    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final Fragment selectedFragment = getFragmentForMenuItem(item.getItemId());
        if (selectedFragment != null) {
            // Back Stack auf dem Main Thread leeren
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().executePendingTransactions();

            // Verzögere den Fragmentwechsel leicht, damit der Pop vollständig abgeschlossen ist
            new Handler(Looper.getMainLooper()).postDelayed(() -> loadFragment(selectedFragment), BACKSTACK_POP_DELAY);
            return true;
        }
        return false;
    }

    /**
     * Gibt das entsprechende Fragment für das ausgewählte Menü-Item zurück.
     */
    private Fragment getFragmentForMenuItem(int itemId) {
        if (itemId == R.id.nav_progression) {
            return new ProgressionFragment();
        } else if (itemId == R.id.nav_nutrition) {
            return new NutritionFragment();
        } else if (itemId == R.id.nav_training) {
            return new TrainingplanFragment();
        } else if (itemId == R.id.nav_exercise) {
            return new ExerciseFragment();
        }
        return null;
    }

    /**
     * Lädt das übergebene Fragment in den Container.
     * commitAllowingStateLoss() wird hier verwendet, um State-Loss-Abstürze zu vermeiden.
     */
    private void loadFragment(Fragment fragment) {
        // Den Container finden
        final View container = findViewById(R.id.fragment_container);
        // Container zunächst unsichtbar machen
        container.setAlpha(0f);

        // Fragment synchron ersetzen
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName())
                .commitNow();

        // Sanftes Einblenden des Containers
        container.animate().alpha(1f).setDuration(200).start();
    }
}
