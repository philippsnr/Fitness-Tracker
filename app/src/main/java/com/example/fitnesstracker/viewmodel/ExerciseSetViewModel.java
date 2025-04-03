package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesstracker.model.ExerciseSet;
import com.example.fitnesstracker.repository.ExerciseSetRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * ViewModel-Klasse für die Verwaltung von Übungssätzen.
 * Diese Klasse bietet Methoden zum Laden der letzten Übungssätze für eine bestimmte Übungszuordnung,
 * Speichern eines neuen Satzes, Laden der Anzahl der Sätze pro Woche und Ermitteln der letzten Satznummer.
 * Alle Operationen werden asynchron im Hintergrund ausgeführt.
 */
public class ExerciseSetViewModel extends AndroidViewModel {
    private final ExerciseSetRepository repository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Konstruktor für das ExerciseSetViewModel.
     *
     * @param application Die Android-Anwendung, die als Kontext dient
     */
    public ExerciseSetViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseSetRepository(application);
    }

    /**
     * Lädt die letzten Sätze für eine bestimmte Übungszuordnung.
     *
     * @param trainingdayExerciseAssignmentId Die ID der Übungszuordnung
     * @param listener Callback für das Ergebnis
     */
    public void loadLastSets(int trainingdayExerciseAssignmentId, OnDataLoadedListener listener) {
        executorService.execute(() -> {
            List<ExerciseSet> sets = repository.getLastSets(trainingdayExerciseAssignmentId);
            listener.onDataLoaded(sets);
        });
    }

    /**
     * Speichert einen neuen Satz in der Datenbank.
     *
     * @param newSet Das zu speichernde ExerciseSet-Objekt
     */
    public void saveNewSet(ExerciseSet newSet) {
        executorService.execute(() -> repository.saveNewSet(newSet));
    }

    /**
     * Lädt die Anzahl der Sätze pro Woche.
     *
     * @param listener Callback für das Ergebnis
     */
    public void loadSetsPerWeek(OnSetsPerWeekLoadedListener listener) {
        executorService.execute(() -> {
            Map<Integer, Integer> setsPerWeek = repository.getSetsPerWeek();
            listener.onSetsPerWeekLoaded(setsPerWeek);
        });
    }

    /**
     * Ermittelt die letzte Satznummer für ein bestimmtes Datum und eine Übung.
     *
     * @param date Das Datum der Übung
     * @param exerciseId Die ID der Übung
     * @param listener Callback für das Ergebnis
     */
    public void getLastSetNumber(String date, int exerciseId, OnLastSetNumberLoadedListener listener) {
        executorService.execute(() -> {
            int lastSetNumber = repository.getLastSetNumber(date, exerciseId);
            listener.onLastSetNumberLoaded(lastSetNumber);
        });
    }

    /**
     * Interface für den Callback beim Laden der Sätze.
     */
    public interface OnDataLoadedListener {
        /**
         * Wird aufgerufen, wenn die Daten geladen wurden.
         *
         * @param sets Die geladene Liste von ExerciseSet-Objekten
         */
        void onDataLoaded(List<ExerciseSet> sets);
    }

    /**
     * Interface für den Callback beim Laden der Wochenstatistik.
     */
    public interface OnSetsPerWeekLoadedListener {
        /**
         * Wird aufgerufen, wenn die Wochenstatistik geladen wurde.
         *
         * @param setsPerWeek Map mit Wochennummer als Schlüssel und Anzahl der Sätze als Wert
         */
        void onSetsPerWeekLoaded(Map<Integer, Integer> setsPerWeek);
    }

    /**
     * Interface für den Callback beim Laden der letzten Satznummer.
     */
    public interface OnLastSetNumberLoadedListener {
        /**
         * Wird aufgerufen, wenn die letzte Satznummer geladen wurde.
         *
         * @param lastSetNumber Die Nummer des letzten Satzes
         */
        void onLastSetNumberLoaded(int lastSetNumber);
    }
}