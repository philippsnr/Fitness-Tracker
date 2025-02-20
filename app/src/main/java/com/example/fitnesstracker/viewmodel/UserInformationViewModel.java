package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.model.UserInformation;
import com.example.fitnesstracker.repository.UserInformationRepository;

import java.util.List;

public class UserInformationViewModel extends AndroidViewModel {

    private final UserInformationRepository repository;
    private UserInformation userInformation;
    public UserInformationViewModel(@NonNull Application application) {
        super(application);
        repository = new UserInformationRepository(application);
    }
    public UserInformation getUserInformationDate(String date) {
        return repository.getUserInformationDate(date);
    }
    public UserInformation getLastUserInformation() {
        return repository.getLatestUserInformation();
    }

    public void writeUserInformation(UserInformation userinfo){
        repository.writeUserInformation(userinfo);
    }

}
