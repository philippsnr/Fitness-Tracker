package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.model.MuscleGroup;
import com.example.fitnesstracker.repository.MuscleGroupRepository;
import java.util.List;

public class MuscleGroupViewModel extends AndroidViewModel {
    private final MuscleGroupRepository repository;
    private List<MuscleGroup> muscleGroups;

    public MuscleGroupViewModel(Application application) {
        super(application);
        repository = new MuscleGroupRepository(application);
        loadMuscleGroups();
    }

    private void loadMuscleGroups() {
        muscleGroups = repository.getAllMuscleGroups();
    }

    public List<MuscleGroup> getMuscleGroups() {
        return muscleGroups;
    }
}
