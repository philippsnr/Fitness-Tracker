package com.example.fitnesstracker.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.fitnesstracker.R;

public class OnboardingGoalFragment extends Fragment {
    private RadioGroup radioGroupGoal;
    private Button buttonNext;
    private OnboardingDataListener dataListener;

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
        View root = inflater.inflate(R.layout.fragment_onboarding_goal, container, false);

        initializeViews(root);
        setupNextButton();

        return root;
    }

    private void initializeViews(View root) {
        radioGroupGoal = root.findViewById(R.id.radioGroupGoal);
        buttonNext = root.findViewById(R.id.buttonNext);
    }

    private void setupNextButton() {
        buttonNext.setOnClickListener(v -> {
            int selectedId = radioGroupGoal.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(getContext(), "Bitte wähle dein Trainingsziel.", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRadioButton = radioGroupGoal.findViewById(selectedId);
            String goal = selectedRadioButton.getText().toString();
            dataListener.onDataCollected("goal", goal);
        });
    }
}
