package com.example.fitnesstracker.ui.nutrition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.OpenFoodFactsResponseModel;

import java.util.List;

public class NutritionListAdapter extends RecyclerView.Adapter<NutritionListAdapter.ProductByNameViewHolder> {

    private List<OpenFoodFactsResponseModel.Product> productList;

    public NutritionListAdapter (List<OpenFoodFactsResponseModel.Product> productList) {
        this.productList = productList;
    }

    @NonNull
    public ProductByNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productbyname, parent, false);
        return new NutritionListAdapter.ProductByNameViewHolder(view);
    }

    public void onBindViewHolder(ProductByNameViewHolder holder, int position) {
        OpenFoodFactsResponseModel.Product product = productList.get(position);
        holder.textViewProductName.setText(product.product_name);

        Glide.with(holder.imageViewProductImage.getContext())
                .load(product.image_url)
                .placeholder(R.drawable.ic_nutrition)
                .error(R.drawable.ic_nutrition)
                .into(holder.imageViewProductImage);
    }

    public int getItemCount() {
        return productList.size();
    }

    public static class ProductByNameViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName;
        ImageView imageViewProductImage;
        public ProductByNameViewHolder (View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textProductName);
            imageViewProductImage = itemView.findViewById(R.id.productImage);
        }
    }
}
