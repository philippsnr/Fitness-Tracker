package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.model.Trainingday;
import com.example.fitnesstracker.repository.TrainingdayRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrainingdayViewModel extends AndroidViewModel {
    private final TrainingdayRepository repository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TrainingdayViewModel(Application application) {
        super(application);
        repository = new TrainingdayRepository(application);
    }

    /**
     * Callback-Interface für asynchrone Operationen.
     */
    public interface OnOperationCompleteListener {
        void onComplete();
        void onError(Exception exception);
    }

    /**
     * Callback-Interface für das Laden von Daten.
     */
    public interface OnDataLoadedListener {
        void onDataLoaded(List<Trainingday> trainingdays);
    }

    /**
     * Lädt alle Trainingstage für einen bestimmten Trainingsplan.
     *
     * @param trainingplanId ID des Trainingsplans
     * @param listener Callback für das geladene Ergebnis
     */
    public void getTrainingdaysForPlan(int trainingplanId, OnDataLoadedListener listener) {
        executorService.execute(() -> {
            List<Trainingday> trainingdays = repository.getTrainingdaysForPlan(trainingplanId);
            listener.onDataLoaded(trainingdays);
        });
    }

    /**
     * Erstellt einen neuen Trainingstag.
     *
     * @param trainingday Das zu erstellende Trainingstag-Objekt
     * @param listener Callback für den Abschluss oder Fehler
     */
    public void createTrainingday(Trainingday trainingday, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                repository.createTrainingday(trainingday);
                listener.onComplete();
            } catch (Exception e) {
                listener.onError(e);
            }
        });
    }

    /**
     * Aktualisiert einen bestehenden Trainingstag.
     *
     * @param trainingday Das zu aktualisierende Trainingstag-Objekt
     * @param listener Callback für den Abschluss oder Fehler
     */
    public void updateTrainingday(Trainingday trainingday, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                repository.updateTrainingday(trainingday);
                listener.onComplete();
            } catch (Exception e) {
                listener.onError(e);
            }
        });
    }

    /**
     * Löscht einen Trainingstag.
     *
     * @param trainingday Das zu löschende Trainingstag-Objekt
     * @param listener Callback für den Abschluss oder Fehler
     */
    public void deleteTrainingday(Trainingday trainingday, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                repository.deleteTrainingday(trainingday);
                listener.onComplete();
            } catch (Exception e) {
                listener.onError(e);
            }
        });
    }
}
