package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.model.ExerciseSet;
import com.example.fitnesstracker.repository.ExerciseSetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExerciseSetViewModel extends AndroidViewModel
{
    private final ExerciseSetRepository repository;
    private final ArrayList<ExerciseSet> exerciseSets = new ArrayList<ExerciseSet>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public ExerciseSetViewModel(@NonNull Application application)
    {
        super(application);
        repository = new ExerciseSetRepository(application);
    }
    public void loadLastSets(int trainingdayExerciseAssignmentId, OnDataLoadedListener listener)
    {
        executorService.execute(() ->
        {
            List<ExerciseSet> sets = repository.getLastSets(trainingdayExerciseAssignmentId);
            listener.onDataLoaded(sets); // if the data is fully loaded -> Callback
        });
    }
    // Method for saving the data of a new set
    public void saveNewSet(ExerciseSet newSet) {
        executorService.execute(() -> {
            repository.saveNewSet(newSet);
        });
    }
    public ArrayList<ExerciseSet> getLastSets() {
        return exerciseSets;
    }
    // Interface for the Callback
    public interface OnDataLoadedListener {
        void onDataLoaded(List<ExerciseSet> sets);
    }
}
