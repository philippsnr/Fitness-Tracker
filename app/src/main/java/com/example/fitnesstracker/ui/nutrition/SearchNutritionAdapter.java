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

    /**
     * Konstruktor des Adapters
     * @param productList Liste an Produkten, die dargestellt werden soll
     * @param listener wartet auf Klick auf Element in Liste
     */
    public SearchNutritionAdapter(List<ProductModel> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    /**
     * Konstruktor des Listeners
     * @param listener wartet auf Klick auf Element in Liste
     */
    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    /**
     * Standardfunktion, die ViewHolder generiert
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return neuer ViewHolder
     */
    @Override
    @NonNull
    public ProductByNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productbyname, parent, false);
        return new SearchNutritionAdapter.ProductByNameViewHolder(view);
    }


    /**
     * Standardfunktion um ViewHolder einzubinden
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ProductByNameViewHolder holder, int position) {
        ProductModel product = productList.get(position);
        holder.bind(product, listener);
    }


    /**
     * Standarddunktion von Adapter
     * @return Größe der Produkliste
     */
    @Override
    public int getItemCount() {
        return productList.size();
    }

    /**
     * ViewHolder des Adapters
     */
    public static class ProductByNameViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName;
        ImageView imageViewProductImage;
        public ProductByNameViewHolder (View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textProductName);
            imageViewProductImage = itemView.findViewById(R.id.productImage);
        }

        /**
         * Definiert, was Adapter darstellen soll und bestimmt, was bei Klicken auf Element passiert
         * @param product Produkt aus Liste
         * @param listener wartet auf Klick auf Element in Liste
         */
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
