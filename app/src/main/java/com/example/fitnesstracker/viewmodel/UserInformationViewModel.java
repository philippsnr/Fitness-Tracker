package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.fitnesstracker.model.UserInformation;
import com.example.fitnesstracker.repository.UserInformationRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;

/**
 * ViewModel-Klasse zur Verwaltung von Benutzerinformationen und Berechnungen des BMI.
 * Diese Klasse interagiert mit dem Repository, um Benutzerinformationen zu laden und den BMI zu berechnen.
 */
public class UserInformationViewModel extends AndroidViewModel {

    private final UserInformationRepository repository;
    private final ExecutorService executorService;

    /**
     * Konstruktor für die {@link UserInformationViewModel} Klasse.
     * Initialisiert das Repository und den ExecutorService.
     *
     * @param application Die Anwendung, die dieses ViewModel erstellt.
     */
    public UserInformationViewModel(@NonNull Application application) {
        super(application);
        repository = new UserInformationRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Lädt alle Benutzerinformationen aus dem Repository.
     *
     * @param listener Der Listener, der nach dem Laden der Daten aufgerufen wird.
     */
    public void getAllUserInformation(OnListDataLoadedListener listener) {
        executorService.execute(() -> {
            List<UserInformation> dataList = repository.getAllUserInformation();
            listener.onDataLoaded(dataList);
        });
    }

    /**
     * Listener-Interface, das nach dem Laden einer Liste von Benutzerinformationen aufgerufen wird.
     */
    public interface OnListDataLoadedListener {
        /**
         * Wird aufgerufen, wenn die Liste der Benutzerinformationen geladen wurde.
         *
         * @param dataList Die geladenen Benutzerinformationen.
         */
        void onDataLoaded(List<UserInformation> dataList);
    }

    /**
     * Listener-Interface, das nach der Berechnung des BMI aufgerufen wird.
     */
    public interface OnBMILoadedListener {
        /**
         * Wird aufgerufen, wenn der BMI berechnet wurde.
         *
         * @param bmi Der berechnete BMI-Wert.
         */
        void onBMILoaded(double bmi);
    }

    /**
     * Berechnet den BMI des Benutzers basierend auf den neuesten Benutzerinformationen.
     *
     * @param listener Der Listener, der nach der Berechnung des BMI aufgerufen wird.
     */
    public void getBMI(OnBMILoadedListener listener) {
        getLastUserInformation(userInfo -> {
            if (userInfo != null) {
                int heightCm = userInfo.getHeight();
                double weightKg = userInfo.getWeight();
                if (heightCm > 0 && weightKg > 0) {
                    double heightM = heightCm / 100.0; // cm → m umwandeln
                    double bmi = weightKg / (heightM * heightM);
                    double bmiRounded = Math.round(bmi * 10.0) / 10.0; // Eine Nachkommastelle
                    listener.onBMILoaded(bmiRounded);
                } else {
                    listener.onBMILoaded(-1); // Fehlerwert
                }
            } else {
                listener.onBMILoaded(-1); // Fehlerwert
            }
        });
    }

    /**
     * Lädt die neuesten Benutzerinformationen aus dem Repository.
     *
     * @param listener Der Listener, der nach dem Laden der Daten aufgerufen wird.
     */
    public void getLastUserInformation(OnDataLoadedListener listener) {
        executorService.execute(() -> {
            UserInformation data = repository.getLatestUserInformation();
            listener.onDataLoaded(data);
        });
    }

    /**
     * Speichert oder aktualisiert die Benutzerinformationen im Repository.
     *
     * @param userinfo Die Benutzerinformationen, die gespeichert oder aktualisiert werden sollen.
     */
    public void writeUserInformation(UserInformation userinfo) {
        executorService.execute(() -> repository.writeUserInformation(userinfo));
    }

    /**
     * Listener-Interface, das nach dem Laden einzelner Benutzerinformationen aufgerufen wird.
     */
    public interface OnDataLoadedListener {
        /**
         * Wird aufgerufen, wenn die Benutzerinformationen geladen wurden.
         *
         * @param data Die geladenen Benutzerinformationen.
         */
        void onDataLoaded(UserInformation data);
    }
}
