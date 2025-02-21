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
    private User user;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void writeDatabaseUser() {
        executorService.execute(() -> repository.writeUser(user));
    }

    public void getDatabaseUser(OnUserLoadedListener listener) {
        executorService.execute(() -> {
            user = repository.getUser();
            listener.onUserLoaded(user);
        });
    }

    public interface OnUserLoadedListener {
        void onUserLoaded(User user);
    }
}