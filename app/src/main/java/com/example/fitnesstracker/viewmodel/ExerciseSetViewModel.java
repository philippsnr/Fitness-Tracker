package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesstracker.repository.ExerciseSetRepository;

public class ExerciseSetViewModel extends AndroidViewModel {
    private final ExerciseSetRepository repository;
    public ExerciseMuscleGroupViewModel(Application application) {
        super(application);
        repository = new ExerciseSetRepository(application);
    }
}
