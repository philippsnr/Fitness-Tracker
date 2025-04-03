package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesstracker.model.NutritiondayNutritionAssignment;
import com.example.fitnesstracker.repository.NutritiondayNutritionAssignmentRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NutritiondayNutritionAssignmentViewModel extends AndroidViewModel {
    private final NutritiondayNutritionAssignmentRepository repository;
    private final ExecutorService executorService;

    /**
     * Konstruktor für das ViewModel.
     * Initialisiert das Repository und einen Single-Thread-Executor für asynchrone Operationen.
     * @param application Die Anwendungskontextinstanz.
     */
    public NutritiondayNutritionAssignmentViewModel(Application application) {
        super(application);
        repository = new NutritiondayNutritionAssignmentRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Lädt die Ernährungszuweisung für einen bestimmten Ernährungstag asynchron.
     * @param nutritiondayId Die ID des Ernährungstags.
     * @param callback       Callback, das nach Abschluss die geladene Zuordnung erhält.
     */
    public void loadNutritiondayNutritionAssignment(int nutritiondayId, Callback<NutritiondayNutritionAssignment> callback) {
        executorService.execute(() -> {
            NutritiondayNutritionAssignment assignment = repository.getNutritionday(nutritiondayId);
            callback.onComplete(assignment);
        });
    }

    /**
     * Speichert die Ernährungszuweisung asynchron in der Datenbank.
     * @param assignment Die zu speichernde Ernährungszuweisung.
     */
    public void saveNutritiondayNutritionAssignment(NutritiondayNutritionAssignment assignment) {
        executorService.execute(() -> repository.setNutritiondayNutritionAssignment(assignment));
    }

    /**
     * Einfache generische Callback-Schnittstelle zur Verarbeitung asynchroner Ergebnisse.
     * @param <T> Der Typ des Rückgabewerts.
     */
    public interface Callback<T> {

        /**
         * Wird aufgerufen, wenn die asynchrone Operation abgeschlossen ist.
         * @param result Das Ergebnis der Operation.
         */
        void onComplete(T result);
    }
}
