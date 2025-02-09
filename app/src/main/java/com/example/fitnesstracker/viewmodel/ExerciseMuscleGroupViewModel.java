package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.repository.ExerciseMuscleGroupRepository;
import java.util.List;

public class ExerciseMuscleGroupViewModel extends AndroidViewModel {
    private final ExerciseMuscleGroupRepository repository;
    private List<Integer> muscleGroups;

    public ExerciseMuscleGroupViewModel(Application application) {
        super(application);
        repository = new ExerciseMuscleGroupRepository(application);
    }

    public void loadMuscleGroups(int exerciseId) {
        muscleGroups = repository.getMuscleGroupsForExercise(exerciseId);
    }

    public List<Integer> getMuscleGroups() {
        return muscleGroups;
    }
}
