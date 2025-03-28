package com.example.fitnesstracker.ui.nutrition;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.OpenFoodFactsResponseModel;

import java.util.List;

public class NutritionFragment extends Fragment {

    private TextView apiFailureText;
    private RecyclerView recyclerView;

    /**
     * Wird ausgeführt wenn UI erstellt wird
     *Erstellt und initialisiert das Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Layout für das Fragment laden
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        apiFailureText = view.findViewById(R.id.connectionText);
        return view;
    }

    /**
     * wird ausgeführt nachdem UI erstellt wurde
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            EditText nutritionNameInput = view.findViewById(R.id.nutritionNameInput);
            Button searchButton = view.findViewById(R.id.btnSearch);
            searchButton.setOnClickListener(v -> showNutritionNamesFromOFFApi(nutritionNameInput));
            recyclerView = view.findViewById(R.id.NutritionsReyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * wird ausgeführt, wenn Fragment sichtbar wird
     */
    public void onStart() {
        super.onStart();

    }

    /**
     * Ruft die API-Aufruf-Funktion auf und übergibt Suchbegriff
     * @param nutritionNameInput eingegebener Suchbegriff
     */
    private void showNutritionNamesFromOFFApi(EditText nutritionNameInput) {
        Log.d("Nutrition", "Test 1");
        String nutritionName = nutritionNameInput.getText().toString();
        Log.d("Nutrition", "Test 2");
        if (!nutritionName.isEmpty()) {
            CallOpenFoodFactsApi.callOpenFoodFactsApiForNutritionNames(nutritionName, new CallOpenFoodFactsApi.Callback() {
                @Override
                public void onSuccess(List<OpenFoodFactsResponseModel.Product> listOfProductsSearchedByNames) {
                    Log.d("Nutrition", "Anfrage hat funktioniert und kann dargestellt werden");
                    showApiCallbackOnUi(listOfProductsSearchedByNames);
                }
                @SuppressLint("SetTextI18n")
                @Override
                public void onFailure(Exception e) {
                    apiFailureText.setText("@string/n_apiFailure");
                    Log.e("Nutrition", "Fehler beim laden der Daten", e);
                }
            });
        }
    }

    private void showApiCallbackOnUi(List<OpenFoodFactsResponseModel.Product> listOfProductsSearchedByNames) {
        NutritionListAdapter nutritionListAdapter = new NutritionListAdapter(listOfProductsSearchedByNames);
        recyclerView.setAdapter(nutritionListAdapter);
    }
}
