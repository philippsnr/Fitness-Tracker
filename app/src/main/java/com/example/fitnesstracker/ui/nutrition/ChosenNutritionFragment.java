package com.example.fitnesstracker.ui.nutrition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.NutrimentsModel;
import com.example.fitnesstracker.model.ProductModel;

public class ChosenNutritionFragment extends Fragment {

    private TextView chosenProductName;
    private TextView productCals;
    private TextView productFats;
    private TextView productCarbs;
    private TextView productProteins;
    private ImageView productImg;
    private ProductModel product;

    /**
     * initialisiert Fragment
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param safedInstancesState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return die Anzeige des Fragments
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle safedInstancesState) {
        return inflater.inflate(R.layout.fragment_chosennutrition, container, false);
    }

    /**
     * wird ausgeführt nachdem UI erstellt wurde und initialisiert Variablen
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        product = extractBundle();
        chosenProductName = view.findViewById(R.id.chosenProductName);
        productCals = view.findViewById(R.id.productCals);
        productFats = view.findViewById(R.id.productFats);
        productCarbs = view.findViewById(R.id.productCarbs);
        productProteins = view.findViewById(R.id.productProteins);
        productImg = view.findViewById(R.id.productImg);
        showProductInformationOnScreen();
    }

    /**
     * extrahiert die empfangenen Daten aus dem Bundle in ein Objekt
     * @return Objekt des Produkts
     */
    private ProductModel extractBundle() {
        ProductModel productModel = new ProductModel();
        NutrimentsModel nutrimentsModel = new NutrimentsModel();
        if (getArguments() != null) {
            productModel.setProductName(getArguments().getString("product_name"));
            productModel.setImageUrl(getArguments().getString("img_url"));
            nutrimentsModel.setEnergyKcal(getArguments().getDouble("product_cals"));
            nutrimentsModel.setFat(getArguments().getDouble("product_fats"));
            nutrimentsModel.setCarbohydrates(getArguments().getDouble("product_carbs"));
            nutrimentsModel.setProteins(getArguments().getDouble("product_proteins"));
            productModel.setNutriments(nutrimentsModel);
        }
        return productModel;
    }

    /**
     * Stellt Daten des Produkts auf UI dar
     */
    private void showProductInformationOnScreen() {
        chosenProductName.setText("Produkt-Name: " + product.getProductName());
        productCals.setText("Kalorien: " + product.getNutriments().getEnergyKcal());
        productCarbs.setText("Kohlenhydrate: " + product.getNutriments().getCarbohydrates());
        productFats.setText("Fette: " + product.getNutriments().getFat());
        productProteins.setText("Proteine: " + product.getNutriments().getProteins());
        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_nutrition)
                .error(R.drawable.ic_nutrition)
                .into(productImg);
    }
}
