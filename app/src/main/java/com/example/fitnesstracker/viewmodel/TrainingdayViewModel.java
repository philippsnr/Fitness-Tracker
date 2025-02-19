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

    public interface OnDataLoadedListener {
        void onDataLoaded(List<Trainingday> trainingdays);
    }

    public void getTrainingdaysForPlan(int trainingplanId, OnDataLoadedListener listener) {
        executorService.execute(() -> {
            List<Trainingday> trainingdays = repository.getTrainingdaysForPlan(trainingplanId);
            listener.onDataLoaded(trainingdays);
        });
    }

    public void createTrainingday(Trainingday trainingday) {
        executorService.execute(() -> repository.createTrainingday(trainingday));
    }

    public void updateTrainingday(Trainingday trainingday) {
        executorService.execute(() -> repository.updateTrainingday(trainingday));
    }

    public void deleteTrainingday(Trainingday trainingday) {
        executorService.execute(() -> repository.deleteTrainingday(trainingday));
    }
}
