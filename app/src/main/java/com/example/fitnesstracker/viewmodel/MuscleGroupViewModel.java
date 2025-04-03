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

/**
 * ViewModel for managing muscle group and exercise data.
 */
public class MuscleGroupViewModel extends AndroidViewModel {
    private final MuscleGroupRepository muscleGroupRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Interface for handling data load completion with generic type support.
     *
     * @param <T> The type of data being loaded.
     */
    public interface OnDataLoadedListener<T> {
        /**
         * Callback method triggered when data is loaded.
         *
         * @param data The loaded data.
         */
        void onDataLoaded(T data);
    }

    /**
     * Constructs the ViewModel and initializes repositories.
     *
     * @param application The application context.
     */
    public MuscleGroupViewModel(Application application) {
        super(application);
        muscleGroupRepository = new MuscleGroupRepository(application);
        exerciseRepository = new ExerciseRepository(application);
    }

    /**
     * Loads all muscle groups asynchronously.
     *
     * @param listener Callback listener to handle the loaded muscle groups.
     */
    public void loadMuscleGroups(OnDataLoadedListener<List<MuscleGroup>> listener) {
        executorService.execute(() -> {
            List<MuscleGroup> result = muscleGroupRepository.getAllMuscleGroups();
            listener.onDataLoaded(result);
        });
    }

    /**
     * Loads exercises for a specific muscle group asynchronously.
     *
     * @param muscleGroupId The ID of the muscle group.
     * @param listener      Callback listener to handle the loaded exercises.
     */
    public void loadExercisesForMuscleGroup(int muscleGroupId, OnDataLoadedListener<List<Exercise>> listener) {
        executorService.execute(() -> {
            List<Exercise> result = exerciseRepository.getExercisesForMuscleGroup(muscleGroupId);
            listener.onDataLoaded(result);
        });
    }
}
