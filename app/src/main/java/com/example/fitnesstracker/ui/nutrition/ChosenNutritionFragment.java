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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle safedInstancesState) {
        return inflater.inflate(R.layout.fragment_chosennutrition, container, false);
    }

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

    public void onStart() {
        super.onStart();
    }


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
