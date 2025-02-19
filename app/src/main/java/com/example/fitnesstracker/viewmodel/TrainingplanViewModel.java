package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import com.example.fitnesstracker.model.Trainingplan;
import com.example.fitnesstracker.repository.TrainingplanRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TrainingplanViewModel {
    private final TrainingplanRepository repository;
    private final ExecutorService executorService;

    public TrainingplanViewModel(Application application) {
        repository = new TrainingplanRepository(application.getApplicationContext());
        executorService = Executors.newSingleThreadExecutor();
    }

    public void loadTrainingplans(Consumer<List<Trainingplan>> callback) {
        executorService.execute(() -> {
            List<Trainingplan> plans = repository.getAllTrainingplans();
            callback.accept(plans); // Daten an UI weitergeben
        });
    }

    public void addTrainingplan(Trainingplan trainingplan, Runnable onComplete) {
        executorService.execute(() -> {
            repository.addTrainingplan(trainingplan);
            onComplete.run(); // Callback nach Abschluss
        });
    }

    public void updateTrainingplan(Trainingplan trainingplan, Runnable onComplete) {
        executorService.execute(() -> {
            repository.updateTrainingplan(trainingplan);
            onComplete.run();
        });
    }

    public void deleteTrainingplan(Trainingplan trainingplan, Runnable onComplete) {
        executorService.execute(() -> {
            repository.deleteTrainingplan(trainingplan);
            onComplete.run();
        });
    }
}
