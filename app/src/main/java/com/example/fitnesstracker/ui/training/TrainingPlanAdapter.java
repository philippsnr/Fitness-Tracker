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

/**
 * Adapter für die Anzeige von Trainingsplänen in einer RecyclerView.
 *
 * Diese Klasse bindet eine Liste von Trainingsplänen an eine RecyclerView und ermöglicht dem Benutzer,
 * auf die einzelnen Elemente zu klicken, um verschiedene Aktionen wie Anzeigen, Bearbeiten oder Löschen eines
 * Trainingsplans auszuführen.
 *
 * Verwendet den ViewHolder-Pattern, um eine effiziente Anzeige der Trainingspläne zu ermöglichen.
 */
public class TrainingPlanAdapter extends RecyclerView.Adapter<TrainingPlanAdapter.PlanViewHolder> {

    private List<Trainingplan> trainingplans;
    private final OnItemClickListener listener;

    /**
     * Interface für Klick-Listener auf die verschiedenen Aktionen eines Trainingsplans.
     * Ermöglicht das Auslösen von Aktionen wie Ansehen, Bearbeiten und Löschen.
     */
    public interface OnItemClickListener {
        /**
         * Wird ausgelöst, wenn auf das Ansehen-Symbol eines Trainingsplans geklickt wird.
         * @param position Die Position des Trainingsplans in der Liste.
         */
        void onViewClick(int position);

        /**
         * Wird ausgelöst, wenn auf das Bearbeiten-Symbol eines Trainingsplans geklickt wird.
         * @param position Die Position des Trainingsplans in der Liste.
         */
        void onEditClick(int position);

        /**
         * Wird ausgelöst, wenn auf das Löschen-Symbol eines Trainingsplans geklickt wird.
         * @param position Die Position des Trainingsplans in der Liste.
         */
        void onDeleteClick(int position);
    }

    /**
     * Konstruktor, der den Adapter mit einer Liste von Trainingsplänen und einem Klick-Listener initialisiert.
     * @param trainingplans Eine Liste von Trainingsplänen, die angezeigt werden sollen.
     * @param listener Der Klick-Listener für Aktionen auf den Trainingsplänen.
     */
    public TrainingPlanAdapter(List<Trainingplan> trainingplans, OnItemClickListener listener) {
        this.trainingplans = trainingplans;
        this.listener = listener;
    }

    /**
     * ViewHolder-Klasse, die die Darstellung eines einzelnen Trainingsplans in der RecyclerView übernimmt.
     */
    public class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView ivView, ivEdit, ivDelete;

        /**
         * Konstruktor für den ViewHolder, der die UI-Komponenten eines Trainingsplans initialisiert.
         * @param itemView Das Layout, das die Ansicht des Trainingsplans darstellt.
         */
        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvPlanName);
            ivView = itemView.findViewById(R.id.ivView);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);

            ivView.setOnClickListener(v -> listener.onViewClick(getAdapterPosition()));
            ivEdit.setOnClickListener(v -> listener.onEditClick(getAdapterPosition()));
            ivDelete.setOnClickListener(v -> listener.onDeleteClick(getAdapterPosition()));
        }

        /**
         * Bindet die Daten eines Trainingsplans an die UI-Komponenten im ViewHolder.
         * @param plan Der Trainingsplan, dessen Daten an die UI gebunden werden sollen.
         */
        public void bind(final Trainingplan plan) {
            name.setText(plan.getName());
        }
    }

    /**
     * Aktualisiert die Liste der Trainingspläne und benachrichtigt den Adapter über die Änderung.
     * @param newPlans Eine neue Liste von Trainingsplänen, die angezeigt werden sollen.
     */
    public void updatePlans(List<Trainingplan> newPlans) {
        if (this.trainingplans == null) {
            this.trainingplans = new ArrayList<>();
        }
        this.trainingplans.clear();
        this.trainingplans.addAll(newPlans);
        notifyDataSetChanged();
    }

    /**
     * Erzeugt den ViewHolder für jedes Trainingsplan-Element in der RecyclerView.
     * @param parent Der ViewGroup, in der der ViewHolder platziert wird.
     * @param viewType Der Typ der Ansicht, die erstellt werden soll (wird hier nicht verwendet).
     * @return Ein neuer ViewHolder für ein Trainingsplan-Element.
     */
    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trainingplan, parent, false);
        return new PlanViewHolder(view);
    }

    /**
     * Bindet die Daten eines Trainingsplans an die Ansicht im ViewHolder für eine bestimmte Position.
     * @param holder Der ViewHolder, der die Ansicht für die aktuelle Position darstellt.
     * @param position Die Position des Elements in der RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        if (trainingplans != null && position < trainingplans.size()) {
            holder.bind(trainingplans.get(position));
        }
    }

    /**
     * Gibt die Anzahl der Elemente in der Liste zurück.
     * @return Die Anzahl der Trainingspläne in der Liste.
     */
    @Override
    public int getItemCount() {
        return trainingplans.size();
    }

    /**
     * Gibt den Trainingsplan an der angegebenen Position in der Liste zurück.
     * @param position Die Position des Trainingsplans in der Liste.
     * @return Der Trainingsplan an der angegebenen Position.
     */
    public Trainingplan getItem(int position) {
        return trainingplans.get(position);
    }
}
