package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesstracker.model.TrainingdayExerciseAssignment;
import com.example.fitnesstracker.repository.TrainingdayExerciseAssignmentRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TrainingdayExerciseAssignmentViewModel extends AndroidViewModel {
    private final TrainingdayExerciseAssignmentRepository repository;
    private final ExecutorService executorService;

    public TrainingdayExerciseAssignmentViewModel(Application application) {
        super(application);
        repository = new TrainingdayExerciseAssignmentRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getTrainingdayExerciseAssignments(int trainingdayId, Consumer<TrainingdayExerciseAssignment> callback) {
        executorService.execute(() -> {
            TrainingdayExerciseAssignment assignment = repository.getTrainingdayExcerciseAssignments(trainingdayId);
            callback.accept(assignment);
        });
    }

    public void getExerciseIdsForTrainingday(int trainingdayId, Consumer<List<Integer>> callback) {
        executorService.execute(() -> {
            List<Integer> exerciseIds = repository.getExerciseIdsForTrainingday(trainingdayId);
            callback.accept(exerciseIds);
        });
    }
}
