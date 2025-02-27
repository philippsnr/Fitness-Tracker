package com.example.fitnesstracker.ui.training;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.Trainingplan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TrainingplanAdapter extends RecyclerView.Adapter<TrainingplanAdapter.PlanViewHolder> {

    private List<Trainingplan> trainingplans;
    private OnItemClickListener listener;

    // Interface für Klick-Listener
    public interface OnItemClickListener {
        void onViewClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public TrainingplanAdapter(List<Trainingplan> trainingplans, OnItemClickListener listener) {
        this.trainingplans = trainingplans;
        this.listener = listener;
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView ivView, ivEdit, ivDelete;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvPlanName);
            ivView = itemView.findViewById(R.id.ivView);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);

            // Setze Klick-Listener für jedes Icon
            ivView.setOnClickListener(v -> listener.onViewClick(getAdapterPosition()));
            ivEdit.setOnClickListener(v -> listener.onEditClick(getAdapterPosition()));
            ivDelete.setOnClickListener(v -> listener.onDeleteClick(getAdapterPosition()));
        }

        public void bind(final Trainingplan plan) {
            name.setText(plan.getName());
        }
    }

    // Methode zum Aktualisieren der Liste
    public void updatePlans(List<Trainingplan> newPlans) {
        if (this.trainingplans == null) {
            this.trainingplans = new ArrayList<>();
        }
        this.trainingplans.clear();
        this.trainingplans.addAll(newPlans);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_training_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        if (trainingplans != null && position < trainingplans.size()) {
            holder.bind(trainingplans.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return trainingplans.size();
    }

    // Methode, um auf ein spezifisches Training zuzugreifen
    public Trainingplan getItem(int position) {
        return trainingplans.get(position);
    }
}
