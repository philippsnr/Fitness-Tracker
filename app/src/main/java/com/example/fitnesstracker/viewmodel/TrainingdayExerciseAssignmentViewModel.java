package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesstracker.repository.TrainingdayExerciseAssignmentRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * ViewModel für die Verwaltung von Zuordnungen zwischen Trainingstagen und Übungen.
 * Bietet asynchrone Operationen für die Interaktion mit der Datenbank.
 */
public class TrainingdayExerciseAssignmentViewModel extends AndroidViewModel {
    private final TrainingdayExerciseAssignmentRepository repository;
    private final ExecutorService executorService;

    /**
     * Initialisiert das ViewModel mit dem gegebenen Application-Kontext.
     *
     * @param application Die Android-Anwendung, die als Kontext dient
     */
    public TrainingdayExerciseAssignmentViewModel(Application application) {
        super(application);
        repository = new TrainingdayExerciseAssignmentRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Ruft die Übungs-IDs für einen bestimmten Trainingstag ab.
     *
     * @param trainingdayId Die ID des Trainingstags
     * @param callback Callback, das die Liste der Übungs-IDs empfängt
     */
    public void getExerciseIdsForTrainingday(int trainingdayId, Consumer<List<Integer>> callback) {
        executorService.execute(() -> {
            List<Integer> exerciseIds = repository.getExerciseIdsForTrainingday(trainingdayId);
            callback.accept(exerciseIds);
        });
    }

    /**
     * Löscht eine Zuordnung zwischen Trainingstag und Übung.
     *
     * @param assignmentId Die ID der zu löschenden Zuordnung
     * @param callback Callback, das nach erfolgreicher Löschung aufgerufen wird
     */
    public void deleteTrainingdayExerciseAssignment(int assignmentId, Runnable callback) {
        executorService.execute(() -> {
            repository.deleteTrainingdayExerciseAssignment(assignmentId);
            callback.run();
        });
    }

    /**
     *  This method is for testing the viewModel
     *
     * @param application Application
     * @return Repo
     */
    protected TrainingdayExerciseAssignmentRepository createRepository(Application application) {
        return new TrainingdayExerciseAssignmentRepository(application);
    }

    /**
     * Erstellt eine neue Zuordnung zwischen Trainingstag und Übung.
     *
     * @param trainingdayId Die ID des Trainingstags
     * @param exerciseId Die ID der Übung
     * @param callback Callback, das die ID der neuen Zuordnung empfängt
     */
    public void addTrainingExerciseAssignment(int trainingdayId, int exerciseId, Consumer<Long> callback) {
        executorService.execute(() -> {
            long newId = repository.addTrainingExerciseAssignment(trainingdayId, exerciseId);
            callback.accept(newId);
        });
    }
}