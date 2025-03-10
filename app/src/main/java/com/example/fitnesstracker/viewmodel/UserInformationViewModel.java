package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.model.UserInformation;
import com.example.fitnesstracker.repository.UserInformationRepository;

import java.text.DecimalFormat;
import java.util.Locale;
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

    public interface OnBMILoadedListener {
        void onBMILoaded(double bmi);
    }

    public void getBMI(OnBMILoadedListener listener) {
        getLastUserInformation(userInfo -> {
            if (userInfo != null) {
                int heightCm = userInfo.getHeight();
                double weightKg = userInfo.getWeight();
                if (heightCm > 0 && weightKg > 0) {
                    double heightM = heightCm / 100.0; // cm → m umwandeln
                    double bmi = weightKg / (heightM * heightM);
                    DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
                    df.applyPattern("#.#"); // Eine Nachkommastelle
                    double bmiRounded = Double.parseDouble(df.format(bmi));
                    listener.onBMILoaded(bmiRounded);
                } else {
                    listener.onBMILoaded(-1); // Fehlerwert
                }
            } else {
                listener.onBMILoaded(-1); // Fehlerwert
            }
        });
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
