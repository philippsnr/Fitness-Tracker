package com.example.fitnesstracker.ui.training;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.viewmodel.TrainingdayViewModel;

import java.util.ArrayList;

/**
 * Fullscreen-Fragment, das die Trainingstage eines Trainingsplans anzeigt.
 * Oben wird der Name des Trainingsplans als Überschrift dargestellt.
 */
public class TrainingdayFragment extends Fragment {

    private static final String ARG_TRAININGPLAN_ID = "trainingplan_id";
    private static final String ARG_TRAININGPLAN_NAME = "trainingplan_name";
    private int trainingplanId;
    private String trainingplanName;
    private TrainingdayViewModel viewModel;
    private RecyclerView recyclerView;
    private TrainingdayAdapter adapter;
    private TextView tvTrainingPlanTitle;

    /**
     * Erzeugt eine neue Instanz dieses Fragments.
     *
     * @param trainingplanId   Die ID des Trainingsplans.
     * @param trainingplanName Der Name des Trainingsplans.
     * @return Eine neue Instanz von TrainingdayFragment.
     */
    public static TrainingdayFragment newInstance(int trainingplanId, String trainingplanName) {
        TrainingdayFragment fragment = new TrainingdayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRAININGPLAN_ID, trainingplanId);
        args.putString(ARG_TRAININGPLAN_NAME, trainingplanName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trainingplanId = getArguments().getInt(ARG_TRAININGPLAN_ID);
            trainingplanName = getArguments().getString(ARG_TRAININGPLAN_NAME);
        }
        // Initialisiere das ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(TrainingdayViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate des Layouts, das einen Header und eine RecyclerView enthält
        View view = inflater.inflate(R.layout.fragment_trainingday, container, false);
        tvTrainingPlanTitle = view.findViewById(R.id.tvTrainingPlanTitle);
        recyclerView = view.findViewById(R.id.recyclerViewTrainingday);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TrainingdayAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Setze die Überschrift des Fragments auf den Namen des Trainingsplans
        if (tvTrainingPlanTitle != null) {
            tvTrainingPlanTitle.setText(trainingplanName);
        }
        // Lade die Trainingstage für den übergebenen Trainingsplan
        viewModel.getTrainingdaysForPlan(trainingplanId, trainingdays ->
                requireActivity().runOnUiThread(() -> adapter.updateData(trainingdays))
        );
    }
}
