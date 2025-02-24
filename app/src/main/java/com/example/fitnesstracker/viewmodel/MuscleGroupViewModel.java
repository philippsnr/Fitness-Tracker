package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.model.MuscleGroup;
import com.example.fitnesstracker.repository.MuscleGroupRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MuscleGroupViewModel extends AndroidViewModel {
    private final MuscleGroupRepository repository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<MuscleGroup> muscleGroups;

    public MuscleGroupViewModel(Application application) {
        super(application);
        repository = new MuscleGroupRepository(application);
    }

    public interface OnDataLoadedListener {
        void onDataLoaded(List<MuscleGroup> muscleGroups);
    }

    public void loadMuscleGroups(OnDataLoadedListener listener) {
        executorService.execute(() -> {
            muscleGroups = repository.getAllMuscleGroups();
            listener.onDataLoaded(muscleGroups); // Callback mit geladenen Daten
        });
    }

    public List<MuscleGroup> getMuscleGroups() {
        return muscleGroups;
    }
}
