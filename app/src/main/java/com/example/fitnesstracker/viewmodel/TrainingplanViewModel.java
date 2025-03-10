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

public class TrainingplanViewModel extends AndroidViewModel {
    private final TrainingplanRepository repository;
    private final ExecutorService executorService;

    public TrainingplanViewModel(@NonNull Application application) {
        super(application);
        repository = new TrainingplanRepository(application.getApplicationContext());
        executorService = Executors.newSingleThreadExecutor();
    }

    public void loadAllTrainingplans(Consumer<List<Trainingplan>> callback, Consumer<Exception> onError) {
        executorService.execute(() -> {
            try {
                List<Trainingplan> plans = repository.getAllTrainingplans();
                callback.accept(plans);
            } catch (Exception e) {
                Log.e("TrainingplanViewModel", "Error loading all training plans", e);
                onError.accept(e);
            }
        });
    }

    public void setActiveTrainingplan(int newActiveTrainingplanId, Runnable onComplete, Consumer<Exception> onError) {
        executorService.execute(() -> {
            try {
                repository.setNewActiveTrainingPlan(newActiveTrainingplanId);
                onComplete.run();
            } catch (Exception e) {
                Log.e("TrainingplanViewModel", "Error setting active training plan", e);
                onError.accept(e);
            }
        });
    }

    public void loadActiveTrainingplan(Consumer<Trainingplan> callback, Consumer<Exception> onError) {
        executorService.execute(() -> {
            try {
                Trainingplan trainingplan = repository.getActiveTrainingplan();
                callback.accept(trainingplan);
            } catch (Exception e) {
                Log.e("TrainingplanViewModel", "Error loading active training plan", e);
                onError.accept(e);
            }
        });
    }

    public void addTrainingplan(Trainingplan trainingplan, Runnable onComplete, Consumer<Exception> onError) {
        executorService.execute(() -> {
            try {
                repository.addTrainingplan(trainingplan);
                onComplete.run();
            } catch (Exception e) {
                Log.e("TrainingplanViewModel", "Error adding training plan", e);
                onError.accept(e);
            }
        });
    }

    public void updateTrainingplan(Trainingplan trainingplan, Runnable onComplete, Consumer<Exception> onError) {
        executorService.execute(() -> {
            try {
                repository.updateTrainingplanName(trainingplan);
                onComplete.run();
            } catch (Exception e) {
                Log.e("TrainingplanViewModel", "Error updating training plan", e);
                onError.accept(e);
            }
        });
    }

    public void deleteTrainingplan(Trainingplan trainingplan, Runnable onComplete, Consumer<Exception> onError) {
        executorService.execute(() -> {
            try {
                repository.deleteTrainingplan(trainingplan);
                onComplete.run();
            } catch (Exception e) {
                Log.e("TrainingplanViewModel", "Error deleting training plan", e);
                onError.accept(e);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        try {
            executorService.shutdownNow(); // Beendet alle laufenden Tasks sofort
        } catch (Exception e) {
            Log.e("TrainingplanViewModel", "Error shutting down executor", e);
        }
    }
}
