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

/**
 * ViewModel for managing exercise-related data.
 */
public class ExerciseViewModel extends AndroidViewModel {
    private final ExerciseRepository repository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Exercise> exercises;

    /**
     * Constructs the ViewModel and initializes the repository.
     *
     * @param application The application context.
     */
    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseRepository(application);
    }

    /**
     * Listener interface for handling data load completion.
     */
    public interface OnDataLoadedListener {
        /**
         * Callback method triggered when exercise data is loaded.
         *
         * @param exercises List of exercises.
         */
        void onDataLoaded(List<Exercise> exercises);
    }

    /**
     * Loads exercises associated with a specific muscle group asynchronously.
     *
     * @param muscleGroupId The ID of the muscle group.
     * @param listener      Callback listener to handle the loaded exercises.
     */
    public void loadExercisesForMuscleGroup(int muscleGroupId, OnDataLoadedListener listener) {
        executorService.execute(() -> {
            exercises = repository.getExercisesForMuscleGroup(muscleGroupId);
            listener.onDataLoaded(exercises);
        });
    }

    /**
     * Loads exercises by a list of exercise IDs asynchronously.
     *
     * @param exerciseIds List of exercise IDs.
     * @param callback    Callback function to handle the loaded exercises.
     */
    public void loadExercisesByIds(List<Integer> exerciseIds, Consumer<List<Exercise>> callback) {
        executorService.execute(() -> {
            List<Exercise> exercises = repository.getExercisesByIds(exerciseIds);
            callback.accept(exercises);
        });
    }
}
