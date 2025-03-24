package com.example.fitnesstracker.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.example.fitnesstracker.R;

public class OnboardingTrainingDaysFragment extends Fragment {
    private SeekBar seekBarTrainingDays;
    private TextView textViewTrainingDays;
    private Button buttonNext;
    private OnboardingDataListener dataListener;
    private int selectedDays = 3; // Standardwert

    @Override
    public void onAttach(android.content.Context context) {
        super.onAttach(context);
        if (context instanceof OnboardingDataListener) {
            dataListener = (OnboardingDataListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnboardingDataListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_onboarding_training_days, container, false);

        initializeViews(root);
        setupSeekBar();
        setupNextButton();

        return root;
    }

    private void initializeViews(View root) {
        seekBarTrainingDays = root.findViewById(R.id.seekBarTrainingDays);
        textViewTrainingDays = root.findViewById(R.id.textViewTrainingDays);
        buttonNext = root.findViewById(R.id.buttonNext);
    }

    private void setupSeekBar() {
        seekBarTrainingDays.setMax(7); // 0 bis 7 Tage
        seekBarTrainingDays.setProgress(selectedDays); // Standardwert auf 3 setzen
        updateTrainingDaysText(selectedDays);

        seekBarTrainingDays.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedDays = progress;
                updateTrainingDaysText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void updateTrainingDaysText(int days) {
        String text = days + " days per week";
        textViewTrainingDays.setText(text);
    }

    private void setupNextButton() {
        buttonNext.setOnClickListener(v -> {
            dataListener.onDataCollected("trainingDays", selectedDays);
        });
    }
}
