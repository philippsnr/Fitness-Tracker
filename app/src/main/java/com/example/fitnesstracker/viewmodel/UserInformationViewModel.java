package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.model.UserInformation;
import com.example.fitnesstracker.repository.UserInformationRepository;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;

public class UserInformationViewModel extends AndroidViewModel {

    private final UserInformationRepository repository;
    private final ExecutorService executorService;

    public UserInformationViewModel(@NonNull Application application) {
        super(application);
        repository = new UserInformationRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getAllUserInformation(OnListDataLoadedListener listener) {
        executorService.execute(() -> {
            List<UserInformation> dataList = repository.getAllUserInformation();
            listener.onDataLoaded(dataList);
        });
    }

    public interface OnListDataLoadedListener {
        void onDataLoaded(List<UserInformation> dataList);
    }

    public void getLastUserInformation(OnDataLoadedListener listener) {
        executorService.execute(() -> {
            UserInformation data = repository.getLatestUserInformation();
            listener.onDataLoaded(data);
        });
    }

    public void writeUserInformation(UserInformation userinfo) {
        executorService.execute(() -> repository.writeUserInformation(userinfo));
    }

    public interface OnDataLoadedListener {
        void onDataLoaded(UserInformation data);
    }
}
