package com.example.fitnesstracker.ui.training;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.Trainingday;
import java.util.List;

public class TrainingDayAdapter extends RecyclerView.Adapter<TrainingDayAdapter.TrainingdayViewHolder> {

    private List<Trainingday> trainingdays;
    private OnItemClickListener listener;

    /**
     * Schnittstelle für Klick-Ereignisse.
     */
    public interface OnItemClickListener {
        void onViewClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    /**
     * Konstruktor.
     *
     * @param trainingdays Liste der Trainingstage.
     * @param listener     Klick-Listener.
     */
    public TrainingDayAdapter(List<Trainingday> trainingdays, OnItemClickListener listener) {
        this.trainingdays = trainingdays;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrainingdayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trainingday, parent, false);
        return new TrainingdayViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingdayViewHolder holder, int position) {
        holder.bind(trainingdays.get(position));
    }

    @Override
    public int getItemCount() {
        return trainingdays.size();
    }

    /**
     * Aktualisiert die Daten des Adapters.
     *
     * @param newTrainingdays Neue Liste der Trainingstage.
     */
    public void updateData(List<Trainingday> newTrainingdays) {
        this.trainingdays = newTrainingdays;
        notifyDataSetChanged();
    }

    /**
     * Gibt den Trainingday an der angegebenen Position zurück.
     *
     * @param position Position des Trainingstags.
     * @return Trainingday-Objekt.
     */
    public Trainingday getItem(int position) {
        return trainingdays.get(position);
    }

    /**
     * ViewHolder für einen Trainingstag.
     */
    static class TrainingdayViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private ImageView ivEdit, ivDelete;
        private CardView cardView;

        /**
         * Konstruktor für den ViewHolder.
         *
         * @param itemView Die Item-View.
         * @param listener Klick-Listener.
         */
        public TrainingdayViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            cardView = (CardView) itemView;
            textViewName = itemView.findViewById(R.id.textViewTrainingdayName);
            ivEdit = itemView.findViewById(R.id.ivEditDay);
            ivDelete = itemView.findViewById(R.id.ivDeleteDay);
            cardView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                    listener.onViewClick(getAdapterPosition());
            });
            ivEdit.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                    listener.onEditClick(getAdapterPosition());
            });
            ivDelete.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                    listener.onDeleteClick(getAdapterPosition());
            });
        }

        /**
         * Bindet den Trainingstag an die Views.
         *
         * @param trainingday Das Trainingday-Objekt.
         */
        public void bind(Trainingday trainingday) {
            textViewName.setText(trainingday.getName());
        }
    }
}
