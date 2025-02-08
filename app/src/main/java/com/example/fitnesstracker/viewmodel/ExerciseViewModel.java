package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.repository.ExerciseRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExerciseViewModel extends AndroidViewModel {
    private final ExerciseRepository repository;
    private final MutableLiveData<List<Exercise>> exercisesLiveData = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseRepository(application);
        loadExercises();
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercisesLiveData;
    }

    // Lädt die Daten in einem separaten Thread
    public void loadExercises() {
        executorService.execute(() -> {
            List<Exercise> exercises = repository.getAllExercises();
            exercisesLiveData.postValue(exercises);
        });
    }
}
