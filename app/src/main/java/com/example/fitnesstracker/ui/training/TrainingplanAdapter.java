package com.example.fitnesstracker.ui.training;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.Trainingplan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TrainingplanAdapter extends RecyclerView.Adapter<TrainingplanAdapter.PlanViewHolder> {

    private List<Trainingplan> plans;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Trainingplan plan);
    }

    public TrainingplanAdapter(List<Trainingplan> plans, OnItemClickListener listener) {
        this.plans = plans;
        this.listener = listener;
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvPlanName);
        }

        public void bind(final Trainingplan plan, final OnItemClickListener listener) {
            name.setText(plan.getName());
            itemView.setOnClickListener(v -> listener.onItemClick(plan));
        }
    }
    public void updatePlans(List<Trainingplan> newPlans) {
        if (this.plans == null) {
            this.plans = new ArrayList<>();
        }
        this.plans.clear();
        this.plans.addAll(newPlans);
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
        if (plans != null && position < plans.size()) {
            holder.bind(plans.get(position), listener);
        }
    }


    @Override
    public int getItemCount() {
        return plans.size();
    }
}
