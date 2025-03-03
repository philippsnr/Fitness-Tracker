package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.model.User;
import com.example.fitnesstracker.repository.UserRepository;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;
    private final ExecutorService executorService;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getUserGoal(OnGoalLoadedListener listener) {
        executorService.execute(() -> {
            String goal = repository.getUserGoal();
            listener.onGoalLoaded(goal);
        });
    }

    public interface OnGoalLoadedListener {
        void onGoalLoaded(String goal);
    }

    public void updateUserGoal(String goal) {
        executorService.execute(() -> repository.updateUserGoal(goal));
    }
}