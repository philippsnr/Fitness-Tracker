package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesstracker.model.Nutritionday;
import com.example.fitnesstracker.repository.NutritiondayRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NutritiondayViewModel extends AndroidViewModel {
    private final NutritiondayRepository repository;
    private final ExecutorService executorService;

    /**
     * Konstruktor für das ViewModel.
     * Initialisiert das Repository und einen Single-Thread-Executor für asynchrone Operationen.
     * @param application Die Anwendungskontextinstanz.
     */
    public NutritiondayViewModel(Application application) {
        super(application);
        repository = new NutritiondayRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Lädt den Ernährungstag für ein bestimmtes Datum asynchron.
     * @param date     Das Datum des Ernährungstags als String.
     * @param callback Callback, das nach Abschluss das geladene Nutritionday-Objekt erhält.
     */
    public void loadNutritionday(String date, Callback<Nutritionday> callback) {
        executorService.execute(() -> {
            Nutritionday nutritionday = repository.getNutritionday(date);
            callback.onComplete(nutritionday);
        });
    }

    /**
     * Speichert den Ernährungstag asynchron in der Datenbank.
     * @param nutritionday Das zu speichernde Nutritionday-Objekt.
     */
    public void saveNutritionday(Nutritionday nutritionday) {
        executorService.execute(() -> repository.setNutritionday(nutritionday));
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
