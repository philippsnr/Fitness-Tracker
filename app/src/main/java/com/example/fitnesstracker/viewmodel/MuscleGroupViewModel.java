package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.model.MuscleGroup;
import com.example.fitnesstracker.repository.ExerciseRepository;
import com.example.fitnesstracker.repository.MuscleGroupRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MuscleGroupViewModel extends AndroidViewModel {
    private final MuscleGroupRepository muscleGroupRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public interface OnDataLoadedListener<T> {
        void onDataLoaded(T data);
    }

    public MuscleGroupViewModel(Application application) {
        super(application);
        muscleGroupRepository = new MuscleGroupRepository(application);
        exerciseRepository = new ExerciseRepository(application);
    }

    // Korrigierte Methoden mit generischen Parametern
    public void loadMuscleGroups(OnDataLoadedListener<List<MuscleGroup>> listener) {
        executorService.execute(() -> {
            List<MuscleGroup> result = muscleGroupRepository.getAllMuscleGroups();
            listener.onDataLoaded(result);
        });
    }

    public void loadExercisesForMuscleGroup(int muscleGroupId, OnDataLoadedListener<List<Exercise>> listener) {
        executorService.execute(() -> {
            List<Exercise> result = exerciseRepository.getExercisesForMuscleGroup(muscleGroupId);
            listener.onDataLoaded(result);
        });
    }
}