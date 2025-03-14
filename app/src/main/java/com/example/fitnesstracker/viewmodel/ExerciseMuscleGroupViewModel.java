package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.repository.ExerciseMuscleGroupRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExerciseMuscleGroupViewModel extends AndroidViewModel {
    private final ExerciseMuscleGroupRepository repository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Integer> muscleGroups;

    public ExerciseMuscleGroupViewModel(Application application) {
        super(application);
        repository = new ExerciseMuscleGroupRepository(application);
    }
    public interface OnDataLoadedListener {
        void onDataLoaded(List<Integer> muscleGroups);
    }
    public void loadMuscleGroups(int exerciseId, OnDataLoadedListener listener) {
        executorService.execute(() -> {
            muscleGroups = repository.getMuscleGroupsForExercise(exerciseId);
            listener.onDataLoaded(muscleGroups);
        });
    }
    public List<Integer> getMuscleGroups() {
        return muscleGroups;
    }
}
