package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.repository.ExerciseRepository;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {
    private final ExerciseRepository repository;
    private List<Exercise> exercises;

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseRepository(application);
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void loadExercisesForMuscleGroup(int muscleGroupId) {
        exercises = repository.getExercisesForMuscleGroup(muscleGroupId);
    }
}
