package com.example.fitnesstracker.ui.training;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.Trainingday;
import com.example.fitnesstracker.model.Trainingplan;

import java.util.List;

public class TrainingdayAdapter extends RecyclerView.Adapter<TrainingdayAdapter.TrainingdayViewHolder> {

    private List<Trainingday> trainingdays;
    private TrainingplanAdapter.OnItemClickListener listener;

    // Interface für Klick-Listener
    public interface OnItemClickListener {
        void onViewClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public TrainingdayAdapter(List<Trainingday> trainingdays) {
        this.trainingdays = trainingdays;
    }

    @NonNull
    @Override
    public TrainingdayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trainingday, parent, false);
        return new TrainingdayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingdayViewHolder holder, int position) {
        Trainingday trainingday = trainingdays.get(position);
        holder.bind(trainingday);
    }

    @Override
    public int getItemCount() {
        return trainingdays.size();
    }

    public void updateData(List<Trainingday> newTrainingdays) {
        this.trainingdays = newTrainingdays;
        notifyDataSetChanged();
    }

    static class TrainingdayViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;

        public TrainingdayViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewTrainingdayName);
        }

        public void bind(Trainingday trainingday) {
            textViewName.setText(trainingday.getName());
        }
    }
}
