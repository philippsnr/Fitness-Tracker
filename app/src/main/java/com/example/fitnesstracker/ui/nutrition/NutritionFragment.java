package com.example.fitnesstracker.ui.nutrition;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fitnesstracker.R;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NutritionFragment extends Fragment {

    /**
     *Erstellt und initialisiert das Fragment
     */
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Layout für das Fragment laden
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        boolean internetConnectionAvailable = checkInternetConnection(executor);
        if (internetConnectionAvailable) {
            EditText nutritionNameInput = view.findViewById(R.id.nutritionNameInput);
            Button searchButton = view.findViewById(R.id.btnSearch);
            searchButton.setOnClickListener(v -> showNutritionNamesFromOFFApi(nutritionNameInput));
        } else {
            createNoConnectionText(view);
        }
        return view;
    }

    /**
     * Erstellt Text, falls keine Internetverbindung verfügbar
     * @param view betreffende View
     */
    private void createNoConnectionText(View view) {
        LinearLayout mainLinearLayout = view.findViewById(R.id.mainLinearLayout);
        TextView noConnectionText = new TextView(requireContext());
        noConnectionText.setText("Keine Internetverbindung verfügbar");
        noConnectionText.setTextSize(18);
        noConnectionText.setPadding(10, 10, 10, 10);
        mainLinearLayout.addView(noConnectionText);
    }

    /**
     * Überprüft über einen Ping, ob Internetverbindung verfügbar ist
     * @return gibt zurück, ob verfügbar oder nicht
     */
    private boolean checkInternetConnection(ExecutorService executor) {
        Callable<Boolean> task = () -> {
            boolean result;
            try {
                Runtime.getRuntime().exec("ping -c 1 8.8.8.8");
                Log.d("Nutrition", "Internetconnection available");
                result = true;
            } catch (IOException e) {
                result = false;
                Log.e("Nutrition", "Internetconnection Request not possible");
            }
            return result;
        };
        Future<Boolean> future = executor.submit(task);
        return getReturnValue(future, executor);
    }

    /**
     * generiert den Rückgabewert für die Funktion checkInternetConnection()
     * @param future
     * @param executor
     * @return
     */
    private boolean getReturnValue(Future<Boolean> future, ExecutorService executor) {
        boolean result = false;
        try {
            result = future.get();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        return result;
    }

    /**
     * Ruft die API-Aufruf-Funktion auf und übergibt Suchbegriff
     * @param nutritionNameInput eingegebener Suchbegriff
     */
    private void showNutritionNamesFromOFFApi(EditText nutritionNameInput) {
        String nutritionName = nutritionNameInput.getText().toString();
        if (!nutritionName.isEmpty()) {
            CallOpenFoodFactsApi apiCall = new CallOpenFoodFactsApi();
            apiCall.callOpenFoodFactsApiForNutritionNames(nutritionName);
        }
    }
}
