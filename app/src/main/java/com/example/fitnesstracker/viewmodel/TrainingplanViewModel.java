package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.example.fitnesstracker.model.Trainingplan;
import com.example.fitnesstracker.repository.TrainingplanRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrainingplanViewModel {
    private final TrainingplanRepository repository;
    private List<Trainingplan> trainingplans;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public TrainingplanViewModel(Application application) {
        repository = new TrainingplanRepository(application.getApplicationContext());
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public List<Trainingplan> getTrainingplans() {
        if (trainingplans == null) {
            loadTrainingplans();
        }
        return trainingplans;
    }

    private void loadTrainingplans() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Trainingplan> plans = repository.getAllTrainingplans();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        trainingplans = plans;
                    }
                });
            }
        });
    }
    public void addTrainingplan(Trainingplan trainingplan) {
        repository.addTrainingplan(trainingplan);
    }

    public void updateTrainingplan(Trainingplan trainingplan) {
        repository.updateTrainingplan(trainingplan);
    }

    public void deleteTrainingplan(Trainingplan trainingplan) {
        repository.deleteTrainingplan(trainingplan);
    }
}
