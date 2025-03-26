package com.example.fitnesstracker.ui.training;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.ExerciseSet;
import com.example.fitnesstracker.viewmodel.ExerciseSetViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment, das die Trainingssets (Sätze) einer Übung anzeigt.
 */
public class TrainingSetsFragment extends Fragment {
    private static final String ARG_ASSIGNMENT_ID = "assignment_id";
    private int assignmentId;
    private RecyclerView recyclerView;
    private TrainingSetsAdapter adapter;
    private ExerciseSetViewModel setViewModel;

    /**
     * Erzeugt eine neue Instanz des Fragments.
     *
     * @param assignmentId Die ID der TrainingdayExerciseAssignment.
     * @return Neue Instanz von TrainingSetsFragment.
     */
    public static TrainingSetsFragment newInstance(int assignmentId) {
        TrainingSetsFragment fragment = new TrainingSetsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ASSIGNMENT_ID, assignmentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assignmentId = getArguments().getInt(ARG_ASSIGNMENT_ID);
        }
        setViewModel = new ViewModelProvider(this).get(ExerciseSetViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainingsets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerViewTrainingExerciseSets);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TrainingSetsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        loadSets();
    }

    /**
     * Lädt die letzten Sets für die gegebene Assignment-ID und aktualisiert den Adapter.
     */
    private void loadSets() {
        setViewModel.loadLastSets(assignmentId, sets ->
                requireActivity().runOnUiThread(() -> adapter.setData(groupByDate(sets)))
        );
    }

    /**
     * Gruppiert eine Liste von ExerciseSet nach Datum.
     *
     * @param sets Liste der geladenen ExerciseSet-Objekte.
     * @return Eine gruppierte Liste als List von TrainingSetsAdapter.ExerciseSetGroup.
     */
    private List<TrainingSetsAdapter.ExerciseSetGroup> groupByDate(List<ExerciseSet> sets) {
        List<TrainingSetsAdapter.ExerciseSetGroup> groups = new ArrayList<>();
        for (ExerciseSet set : sets) {
            boolean found = false;
            for (TrainingSetsAdapter.ExerciseSetGroup group : groups) {
                if (group.getDate().equals(set.getDate())) {
                    group.getSets().add(set);
                    found = true;
                    break;
                }
            }
            if (!found) {
                List<ExerciseSet> newGroupList = new ArrayList<>();
                newGroupList.add(set);
                groups.add(new TrainingSetsAdapter.ExerciseSetGroup(set.getDate(), newGroupList));
            }
        }
        return groups;
    }
}
