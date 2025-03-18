package com.example.fitnesstracker.ui.training;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.Trainingday;

import java.util.List;

public class TrainingDayAdapter extends RecyclerView.Adapter<TrainingDayAdapter.TrainingdayViewHolder> {

    private List<Trainingday> trainingdays;
    private OnItemClickListener listener;

    /**
     * Interface für Klick-Listener.
     */
    public interface OnItemClickListener {
        void onViewClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    /**
     * Konstruktor für den Adapter.
     *
     * @param trainingdays Die Liste der Trainingstage.
     * @param listener     Der Listener für Klick-Ereignisse.
     */
    public TrainingDayAdapter(List<Trainingday> trainingdays, OnItemClickListener listener) {
        this.trainingdays = trainingdays;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrainingdayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trainingday, parent, false);
        return new TrainingdayViewHolder(view, listener);
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

    /**
     * Aktualisiert die Daten des Adapters.
     *
     * @param newTrainingdays Die neue Liste der Trainingstage.
     */
    public void updateData(List<Trainingday> newTrainingdays) {
        this.trainingdays = newTrainingdays;
        notifyDataSetChanged();
    }

    /**
     * Gibt das Trainingday-Objekt an der angegebenen Position zurück.
     *
     * @param position Die Position des Trainingstags.
     * @return Das Trainingday-Objekt.
     */
    public Trainingday getItem(int position) {
        return trainingdays.get(position);
    }

    /**
     * ViewHolder für die Trainingstage.
     */
    static class TrainingdayViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private ImageView ivView, ivEdit, ivDelete;

        /**
         * Konstruktor für den ViewHolder.
         *
         * @param itemView Die View des ViewHolders.
         * @param listener Der Listener für Klick-Ereignisse.
         */
        public TrainingdayViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            initializeViews(itemView);
            setupClickListeners(listener);
        }

        /**
         * Initialisiert die Views.
         *
         * @param itemView Die View des ViewHolders.
         */
        private void initializeViews(View itemView) {
            textViewName = itemView.findViewById(R.id.textViewTrainingdayName);
            ivView = itemView.findViewById(R.id.ivViewDay);
            ivEdit = itemView.findViewById(R.id.ivEditDay);
            ivDelete = itemView.findViewById(R.id.ivDeleteDay);
        }

        /**
         * Setzt die Klick-Listener für die ImageViews.
         *
         * @param listener Der Listener für Klick-Ereignisse.
         */
        private void setupClickListeners(OnItemClickListener listener) {
            ivView.setOnClickListener(v -> handleViewClick(listener));
            ivEdit.setOnClickListener(v -> handleEditClick(listener));
            ivDelete.setOnClickListener(v -> handleDeleteClick(listener));
        }

        /**
         * Verarbeitet den Klick auf die "Anzeigen"-Schaltfläche.
         *
         * @param listener Der Listener für Klick-Ereignisse.
         */
        private void handleViewClick(OnItemClickListener listener) {
            if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                listener.onViewClick(getAdapterPosition());
            }
        }

        /**
         * Verarbeitet den Klick auf die "Bearbeiten"-Schaltfläche.
         *
         * @param listener Der Listener für Klick-Ereignisse.
         */
        private void handleEditClick(OnItemClickListener listener) {
            if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                listener.onEditClick(getAdapterPosition());
            }
        }

        /**
         * Verarbeitet den Klick auf die "Löschen"-Schaltfläche.
         *
         * @param listener Der Listener für Klick-Ereignisse.
         */
        private void handleDeleteClick(OnItemClickListener listener) {
            if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                listener.onDeleteClick(getAdapterPosition());
            }
        }

        /**
         * Bindet die Daten an die Views.
         *
         * @param trainingday Das Trainingday-Objekt.
         */
        public void bind(Trainingday trainingday) {
            textViewName.setText(trainingday.getName());
        }
    }
}