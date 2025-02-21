package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesstracker.model.NutritiondayNutritionAssignment;
import com.example.fitnesstracker.repository.NutritiondayNutritionAssignmentRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NutritiondayNutritionAssignmentViewModel extends AndroidViewModel {
    private final NutritiondayNutritionAssignmentRepository repository;
    private final ExecutorService executorService;

    public NutritiondayNutritionAssignmentViewModel(Application application) {
        super(application);
        repository = new NutritiondayNutritionAssignmentRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void loadNutritiondayNutritionAssignment(int nutritiondayId, Callback<NutritiondayNutritionAssignment> callback) {
        executorService.execute(() -> {
            NutritiondayNutritionAssignment assignment = repository.getNutritionday(nutritiondayId);
            callback.onComplete(assignment);
        });
    }

    public void saveNutritiondayNutritionAssignment(NutritiondayNutritionAssignment assignment) {
        executorService.execute(() -> repository.setNutritiondayNutritionAssignment(assignment));
    }

    public interface Callback<T> {
        void onComplete(T result);
    }
}
