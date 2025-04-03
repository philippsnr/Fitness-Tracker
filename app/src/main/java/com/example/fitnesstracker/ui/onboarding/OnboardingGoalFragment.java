package com.example.fitnesstracker.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.fitnesstracker.R;

/**
 * Fragment for selecting the user's training goal during onboarding.
 */
public class OnboardingGoalFragment extends Fragment {
    private RadioGroup radioGroupGoal;
    private Button buttonNext;
    private OnboardingDataListener dataListener;

    /**
     * Attaches the fragment to the context and verifies that it implements OnboardingDataListener.
     *
     * @param context The context to which the fragment is attached.
     */
    @Override
    public void onAttach(@NonNull android.content.Context context) {
        super.onAttach(context);
        if (context instanceof OnboardingDataListener) {
            dataListener = (OnboardingDataListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnboardingDataListener");
        }
    }

    /**
     * Inflates the layout, initializes views, and sets up the next button.
     *
     * @param inflater LayoutInflater to inflate the view.
     * @param container The parent container.
     * @param savedInstanceState Previously saved state.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_onboarding_goal, container, false);
        initializeViews(root);
        setupNextButton();
        return root;
    }

    /**
     * Initializes the UI components.
     *
     * @param root The root view of the fragment.
     */
    private void initializeViews(View root) {
        radioGroupGoal = root.findViewById(R.id.radioGroupGoal);
        buttonNext = root.findViewById(R.id.buttonNext);
    }

    /**
     * Sets up the next button to validate the selection and notify the data listener.
     */
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
