package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesstracker.model.Trainingplan;
import com.example.fitnesstracker.repository.TrainingplanRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * ViewModel-Klasse für die Verwaltung von Trainingsplänen.
 * Diese Klasse bietet Methoden, um Trainingspläne aus der Datenbank zu laden, einen Trainingsplan als aktiv zu setzen,
 * neue Trainingspläne hinzuzufügen, bestehende Trainingspläne zu aktualisieren oder zu löschen.
 * Alle Operationen werden asynchron im Hintergrund ausgeführt.
 */
public class TrainingplanViewModel extends AndroidViewModel {
    private final TrainingplanRepository repository;
    private final ExecutorService executorService;

    /**
     * Konstruktor für das ViewModel. Initialisiert das Repository und den ExecutorService.
     *
     * @param application Der Anwendungs-Kontext
     */
    public TrainingplanViewModel(@NonNull Application application) {
        super(application);
        repository = new TrainingplanRepository(application.getApplicationContext());
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Lädt alle Trainingspläne aus der Datenbank.
     *
     * @param callback Callback für die geladenen Trainingspläne
     * @param onError Callback für Fehlerbehandlung
     */
    public void loadAllTrainingplans(Consumer<List<Trainingplan>> callback, Consumer<Exception> onError) {
        executorService.execute(() -> {
            try {
                List<Trainingplan> plans = repository.getAllTrainingplans();
                callback.accept(plans);
            } catch (Exception e) {
                Log.e("TrainingplanViewModel", "Fehler beim Laden aller Trainingspläne", e);
                onError.accept(e);
            }
        });
    }

    /**
     * Setzt einen Trainingsplan als aktiv.
     *
     * @param newActiveTrainingplanId ID des neuen aktiven Trainingsplans
     * @param onComplete Callback für erfolgreichen Abschluss
     * @param onError Callback für Fehlerbehandlung
     */
    public void setActiveTrainingplan(int newActiveTrainingplanId, Runnable onComplete, Consumer<Exception> onError) {
        executorService.execute(() -> {
            try {
                repository.setNewActiveTrainingPlan(newActiveTrainingplanId);
                onComplete.run();
            } catch (Exception e) {
                Log.e("TrainingplanViewModel", "Fehler beim Setzen des aktiven Trainingsplans", e);
                onError.accept(e);
            }
        });
    }

    /**
     * Lädt den aktuell aktiven Trainingsplan.
     *
     * @param callback Callback für den geladenen Trainingsplan
     * @param onError Callback für Fehlerbehandlung
     */
    public void loadActiveTrainingplan(Consumer<Trainingplan> callback, Consumer<Exception> onError) {
        executorService.execute(() -> {
            try {
                Trainingplan trainingplan = repository.getActiveTrainingplan();
                callback.accept(trainingplan);
            } catch (Exception e) {
                Log.e("TrainingplanViewModel", "Fehler beim Laden des aktiven Trainingsplans", e);
                onError.accept(e);
            }
        });
    }

    /**
     * Fügt einen neuen Trainingsplan hinzu.
     *
     * @param trainingplan Der hinzuzufügende Trainingsplan
     * @param onComplete Callback für erfolgreichen Abschluss
     * @param onError Callback für Fehlerbehandlung
     */
    public void addTrainingplan(Trainingplan trainingplan, Runnable onComplete, Consumer<Exception> onError) {
        executorService.execute(() -> {
            try {
                repository.addTrainingplan(trainingplan);
                onComplete.run();
            } catch (Exception e) {
                Log.e("TrainingplanViewModel", "Fehler beim Hinzufügen eines Trainingsplans", e);
                onError.accept(e);
            }
        });
    }

    /**
     * Aktualisiert einen bestehenden Trainingsplan.
     *
     * @param trainingplan Der zu aktualisierende Trainingsplan
     * @param onComplete Callback für erfolgreichen Abschluss
     * @param onError Callback für Fehlerbehandlung
     */
    public void updateTrainingplan(Trainingplan trainingplan, Runnable onComplete, Consumer<Exception> onError) {
        executorService.execute(() -> {
            try {
                repository.updateTrainingplanName(trainingplan);
                onComplete.run();
            } catch (Exception e) {
                Log.e("TrainingplanViewModel", "Fehler beim Aktualisieren eines Trainingsplans", e);
                onError.accept(e);
            }
        });
    }

    /**
     * Löscht einen Trainingsplan.
     *
     * @param trainingplan Der zu löschende Trainingsplan
     * @param onComplete Callback für erfolgreichen Abschluss
     * @param onError Callback für Fehlerbehandlung
     */
    public void deleteTrainingplan(Trainingplan trainingplan, Runnable onComplete, Consumer<Exception> onError) {
        executorService.execute(() -> {
            try {
                repository.deleteTrainingplan(trainingplan);
                onComplete.run();
            } catch (Exception e) {
                Log.e("TrainingplanViewModel", "Fehler beim Löschen eines Trainingsplans", e);
                onError.accept(e);
            }
        });
    }
}