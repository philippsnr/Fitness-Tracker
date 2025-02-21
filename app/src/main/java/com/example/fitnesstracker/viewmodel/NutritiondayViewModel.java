package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesstracker.model.Nutritionday;
import com.example.fitnesstracker.repository.NutritiondayRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NutritiondayViewModel extends AndroidViewModel {
    private final NutritiondayRepository repository;
    private final ExecutorService executorService;

    public NutritiondayViewModel(Application application) {
        super(application);
        repository = new NutritiondayRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void loadNutritionday(String date, Callback<Nutritionday> callback) {
        executorService.execute(() -> {
            Nutritionday nutritionday = repository.getNutritionday(date);
            callback.onComplete(nutritionday);
        });
    }

    public void saveNutritionday(Nutritionday nutritionday) {
        executorService.execute(() -> repository.setNutritionday(nutritionday));
    }

    public interface Callback<T> {
        void onComplete(T result);
    }
}
