package com.example.fitnesstracker.ui.nutrition;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.OpenFoodFactsResponseModel;
import com.example.fitnesstracker.ui.nutrition.CallOpenFoodFactsApi;

public class NutritionFragment extends Fragment {

    /**
     *Erstellt und initialisiert das Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Layout für das Fragment laden
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);

        EditText nutritionNameInput = view.findViewById(R.id.nutritionNameInput);
        Button searchButton = view.findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(v -> showNutritionNamesFromOFFApi(nutritionNameInput));

        return view;
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
