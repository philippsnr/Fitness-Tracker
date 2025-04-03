package com.example.fitnesstracker.ui.training;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.ExerciseSet;
import java.time.LocalDate;
import java.util.List;

/**
 * Adapter für die Anzeige von Trainingssets, gruppiert nach Datum.
 */
public class TrainingSetsAdapter extends RecyclerView.Adapter<TrainingSetsAdapter.SetGroupViewHolder> {

    private List<ExerciseSetGroup> groups;

    /**
     * Konstruktor des Adapters.
     *
     * @param groups Liste der gruppierten ExerciseSet-Gruppen.
     */
    public TrainingSetsAdapter(List<ExerciseSetGroup> groups) {
        this.groups = groups;
    }

    /**
     * Aktualisiert die angezeigten Daten.
     *
     * @param groups Neue gruppierte Daten.
     */
    public void setData(List<ExerciseSetGroup> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SetGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trainingsets, parent, false);
        return new SetGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetGroupViewHolder holder, int position) {
        holder.bind(groups.get(position));
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    /**
     * ViewHolder für eine ExerciseSet-Gruppe.
     */
    public static class SetGroupViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDateHeader;
        private final LinearLayout llSetsContainer;

        /**
         * Konstruktor des ViewHolders.
         *
         * @param itemView Die Item-View.
         */
        public SetGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateHeader = itemView.findViewById(R.id.tvDateHeader);
            llSetsContainer = itemView.findViewById(R.id.llExerciseSets);
        }

        /**
         * Bindet die Daten der Gruppe an die View.
         *
         * @param group Die ExerciseSet-Gruppe.
         */
        public void bind(ExerciseSetGroup group) {
            tvDateHeader.setText(group.getDate().toString());
            llSetsContainer.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
            for (ExerciseSet set : group.getSets()) {
                View row = inflater.inflate(R.layout.item_trainingsets_row, llSetsContainer, false);
                ((TextView) row.findViewById(R.id.tvSetNumber)).setText("Set " + set.getSetNumber() + ":");
                ((TextView) row.findViewById(R.id.tvWeight)).setText(set.getWeight() + " kg");
                ((TextView) row.findViewById(R.id.tvRepetitions)).setText(set.getRepetition() + " reps");
                llSetsContainer.addView(row);
            }
        }
    }

    /**
     * Modellklasse zur Gruppierung von ExerciseSet-Objekten nach Datum.
     */
    public static class ExerciseSetGroup {
        private final LocalDate date;
        private final List<ExerciseSet> sets;

        /**
         * Konstruktor für die Gruppierung.
         *
         * @param date Das Datum der Gruppe.
         * @param sets Die zugehörigen Sets.
         */
        public ExerciseSetGroup(LocalDate date, List<ExerciseSet> sets) {
            this.date = date;
            this.sets = sets;
        }

        public LocalDate getDate() { return date; }
        public List<ExerciseSet> getSets() { return sets; }
    }
}
