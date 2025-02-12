package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.model.ExerciseSet;
import com.example.fitnesstracker.repository.ExerciseSetRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExerciseSetViewModel extends AndroidViewModel
{
    private final ExerciseSetRepository repository;
    private final MutableLiveData<List<ExerciseSet>> exerciseSets = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public ExerciseSetViewModel(@NonNull Application application)
    {
        super(application);
        repository = new ExerciseSetRepository(application);
    }
    public void loadLastSets(int trainingdayExerciseAssignmentId)
    {
        executorService.execute(() ->
        {
            List<ExerciseSet> sets = repository.getLastSets(trainingdayExerciseAssignmentId);
            exerciseSets.postValue(sets); // LiveData im UI-Thread aktualisieren
        });
    }
    public LiveData<List<ExerciseSet>> getLastSets() {
        return exerciseSets;
    }
}
