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
import com.example.fitnesstracker.model.ProductModel;

import java.util.List;

public class SearchNutritionAdapter extends RecyclerView.Adapter<SearchNutritionAdapter.ProductByNameViewHolder> {

    private List<ProductModel> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProductModel  product);
    }

    public SearchNutritionAdapter(List<ProductModel> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    public ProductByNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productbyname, parent, false);
        return new SearchNutritionAdapter.ProductByNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductByNameViewHolder holder, int position) {
        ProductModel product = productList.get(position);
        holder.bind(product, listener);
    }

    @Override
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

        public void bind(final ProductModel product, final OnItemClickListener listener) {
            textViewProductName.setText(product.getProductName());
            Glide.with(itemView.getContext())
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.ic_nutrition)
                    .error(R.drawable.ic_nutrition)
                    .into(imageViewProductImage);

            itemView.setOnClickListener(v -> listener.onItemClick(product));
        }
    }
}
