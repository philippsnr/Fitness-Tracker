package com.example.fitnesstracker.ui.exercise;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.fitnesstracker.R;

public class ExerciseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Layout für das Fragment laden
        View view = inflater.inflate(R.layout.fragment_muscle_groups, container, false);

        return view;
    }
}
