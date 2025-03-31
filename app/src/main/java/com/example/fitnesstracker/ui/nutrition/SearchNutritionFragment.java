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
import com.example.fitnesstracker.model.ProductModel;

import java.io.Serializable;
import java.util.List;

public class SearchNutritionFragment extends Fragment implements SearchNutritionAdapter.OnItemClickListener {

    private TextView apiFailureText;
    private RecyclerView recyclerView;

    /**
     * Wird ausgeführt wenn UI erstellt wird
     * Erstellt und initialisiert das Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Layout für das Fragment laden
        return inflater.inflate(R.layout.fragment_nutrition, container, false);
    }

    /**
     * wird ausgeführt nachdem UI erstellt wurde
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        apiFailureText = view.findViewById(R.id.connectionText);
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
        String nutritionName = nutritionNameInput.getText().toString();
        if (!nutritionName.isEmpty()) {
            CallOpenFoodFactsApi.callOpenFoodFactsApiForNutritionNames(nutritionName, new CallOpenFoodFactsApi.Callback() {
                @Override
                public void onSuccess(List<ProductModel> listOfProductsSearchedByNames) {
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

    private void showApiCallbackOnUi(List<ProductModel> listOfProductsSearchedByNames) {
        SearchNutritionAdapter nutritionListAdapter = new SearchNutritionAdapter(listOfProductsSearchedByNames, this);
        recyclerView.setAdapter(nutritionListAdapter);
    }

    public void onItemClick(ProductModel product) {
        Log.d("Nutrition", "OnClick ausgeführt");
        openChosenNutritionFragment(product);
    }

    private void openChosenNutritionFragment(ProductModel product) {
        Bundle bundle = createBundle(product);

        ChosenNutritionFragment chosenNutritionFragment = new ChosenNutritionFragment();
        chosenNutritionFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, chosenNutritionFragment)
                .addToBackStack(null)
                .commit();

    }

    @NonNull
    private static Bundle createBundle(ProductModel product) {
        Bundle bundle = new Bundle();
        bundle.putString("product_name", product.getProductName());
        bundle.putString("img_url", product.getImageUrl());
        bundle.putDouble("product_cals", product.getNutriments().getEnergyKcal());
        bundle.putDouble("product_fats", product.getNutriments().getFat());
        bundle.putDouble("product_carbs", product.getNutriments().getCarbohydrates());
        bundle.putDouble("product_proteins", product.getNutriments().getProteins());
        return bundle;
    }
}
