package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.model.User;
import com.example.fitnesstracker.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;
    private User user;
    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }
    public void createUser() {
        repository.createUser(user);
    }
}
