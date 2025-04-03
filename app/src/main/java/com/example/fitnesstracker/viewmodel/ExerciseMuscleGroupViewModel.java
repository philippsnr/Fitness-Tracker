package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.repository.ExerciseMuscleGroupRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel for managing the relationship between exercises and muscle groups.
 */
public class ExerciseMuscleGroupViewModel extends AndroidViewModel {
    private final ExerciseMuscleGroupRepository repository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Integer> muscleGroups;

    /**
     * Constructs the ViewModel and initializes the repository.
     *
     * @param application The application context.
     */
    public ExerciseMuscleGroupViewModel(Application application) {
        super(application);
        repository = new ExerciseMuscleGroupRepository(application);
    }

    /**
     * Listener interface for handling data load completion.
     */
    public interface OnDataLoadedListener {
        /**
         * Callback method triggered when muscle group data is loaded.
         *
         * @param muscleGroups List of muscle group IDs.
         */
        void onDataLoaded(List<Integer> muscleGroups);
    }

    /**
     * Loads the muscle groups associated with a given exercise asynchronously.
     *
     * @param exerciseId The ID of the exercise.
     * @param listener   Callback listener to handle the loaded data.
     */
    public void loadMuscleGroups(int exerciseId, OnDataLoadedListener listener) {
        executorService.execute(() -> {
            muscleGroups = repository.getMuscleGroupsForExercise(exerciseId);
            listener.onDataLoaded(muscleGroups);
        });
    }

    /**
     * Retrieves the list of muscle groups currently stored in memory.
     *
     * @return List of muscle group IDs.
     */
    public List<Integer> getMuscleGroups() {
        return muscleGroups;
    }
}
