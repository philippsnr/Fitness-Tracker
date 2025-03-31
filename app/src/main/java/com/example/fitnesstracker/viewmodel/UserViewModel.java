package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.repository.UserRepository;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel-Klasse für Benutzer.
 * Verwaltet Benutzerinformationen und führt datenbankbezogene Operationen in einem separaten Thread aus.
 */
public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;
    private final ExecutorService executorService;

    /**
     * Konstruktor für das UserViewModel.
     *
     * @param application Die Anwendungsklasse, die den Kontext bereitstellt.
     */
    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Ruft das Fitnessziel des Benutzers asynchron aus der Datenbank ab.
     *
     * @param listener Callback, der aufgerufen wird, wenn das Ziel geladen wurde.
     */
    public void getUserGoal(OnGoalLoadedListener listener) {
        executorService.execute(() -> {
            String goal = repository.getUserGoal();
            listener.onGoalLoaded(goal);
        });
    }

    /**
     * Schnittstelle für den Rückruf, wenn das Benutzerziel geladen wurde.
     */
    public interface OnGoalLoadedListener {
        /**
         * Methode, die aufgerufen wird, wenn das Ziel erfolgreich aus der Datenbank geladen wurde.
         *
         * @param goal Das geladene Fitnessziel des Benutzers.
         */
        void onGoalLoaded(String goal);
    }

    /**
     * Aktualisiert das Fitnessziel des Benutzers in der Datenbank asynchron.
     *
     * @param goal Das neue Fitnessziel.
     */
    public void updateUserGoal(String goal) {
        executorService.execute(() -> repository.updateUserGoal(goal));
    }
}
