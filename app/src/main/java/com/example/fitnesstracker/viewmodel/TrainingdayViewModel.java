package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesstracker.model.Trainingday;
import com.example.fitnesstracker.repository.TrainingdayRepository;

import java.util.List;

public class TrainingdayViewModel extends AndroidViewModel {
    private final TrainingdayRepository repository;

    public TrainingdayViewModel(Application application) {
        super(application);
        repository = new TrainingdayRepository(application);
    }

    public List<Trainingday> getTrainingdaysForPlan(int trainingplanId) {
        return repository.getTrainingdaysForPlan(trainingplanId);
    }

    public void createTrainingday(Trainingday trainingday) {
        repository.createTrainingday(trainingday);
    }

    public void updateTrainingday(Trainingday trainingday) {
        repository.updateTrainingday(trainingday);
    }

    public void deleteTrainingday(Trainingday trainingday) {
        repository.deleteTrainingday(trainingday);
    }
}
