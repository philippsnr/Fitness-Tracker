package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.repository.ExerciseRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ExerciseViewModel extends AndroidViewModel {
    private final ExerciseRepository repository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Exercise> exercises;

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseRepository(application);
    }

    public interface OnDataLoadedListener {
        void onDataLoaded(List<Exercise> exercises);
    }

    public void loadExercisesForMuscleGroup(int muscleGroupId, OnDataLoadedListener listener) {
        executorService.execute(() -> {
            exercises = repository.getExercisesForMuscleGroup(muscleGroupId);
            listener.onDataLoaded(exercises); // Callback mit den geladenen Daten
        });
    }

    public void loadExercisesByIds(List<Integer> exerciseIds, Consumer<List<Exercise>> callback) {
        executorService.execute(() -> {
            List<Exercise> exercises = repository.getExercisesByIds(exerciseIds);
            callback.accept(exercises);
        });
    }
}
