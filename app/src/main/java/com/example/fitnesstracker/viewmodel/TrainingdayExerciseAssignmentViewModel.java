package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesstracker.model.TrainingdayExcerciseAssignment;
import com.example.fitnesstracker.repository.TrainingdayExcerciseAssignmentRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TrainingdayExerciseAssignmentViewModel extends AndroidViewModel {
    private final TrainingdayExcerciseAssignmentRepository repository;
    private final ExecutorService executorService;

    public TrainingdayExerciseAssignmentViewModel(Application application) {
        super(application);
        repository = new TrainingdayExcerciseAssignmentRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getTrainingdayExerciseAssignments(int trainingdayId, Consumer<TrainingdayExcerciseAssignment> callback) {
        executorService.execute(() -> {
            TrainingdayExcerciseAssignment assignment = repository.getTrainingdayExcerciseAssignments(trainingdayId);
            callback.accept(assignment);
        });
    }
}
