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

/**
 * Fragment for selecting the number of training days per week during onboarding.
 */
public class OnboardingTrainingDaysFragment extends Fragment {
    private SeekBar seekBarTrainingDays;
    private TextView textViewTrainingDays;
    private Button buttonNext;
    private OnboardingDataListener dataListener;
    private int selectedDays = 3;

    /**
     * Attaches the fragment to the context and ensures that it implements OnboardingDataListener.
     *
     * @param context The context to which the fragment is attached.
     */
    @Override
    public void onAttach(android.content.Context context) {
        super.onAttach(context);
        if (context instanceof OnboardingDataListener) {
            dataListener = (OnboardingDataListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnboardingDataListener");
        }
    }

    /**
     * Inflates the layout, initializes UI components, and sets up the seek bar and next button.
     *
     * @param inflater LayoutInflater to inflate the view.
     * @param container The parent container.
     * @param savedInstanceState Previously saved state.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_onboarding_training_days, container, false);
        initializeViews(root);
        setupSeekBar();
        setupNextButton();
        return root;
    }

    /**
     * Initializes the UI components.
     *
     * @param root The root view of the fragment.
     */
    private void initializeViews(View root) {
        seekBarTrainingDays = root.findViewById(R.id.seekBarTrainingDays);
        textViewTrainingDays = root.findViewById(R.id.textViewTrainingDays);
        buttonNext = root.findViewById(R.id.buttonNext);
    }

    /**
     * Configures the seek bar for selecting training days and updates the corresponding text view.
     */
    private void setupSeekBar() {
        seekBarTrainingDays.setMax(7);
        seekBarTrainingDays.setProgress(selectedDays);
        updateTrainingDaysText(selectedDays);

        seekBarTrainingDays.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * Updates the selected number of training days as the seek bar progress changes.
             *
             * @param seekBar The seek bar instance.
             * @param progress The current progress.
             * @param fromUser True if the change was initiated by the user.
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedDays = progress;
                updateTrainingDaysText(progress);
            }

            /**
             * Called when the user starts touching the seek bar.
             *
             * @param seekBar The seek bar instance.
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            /**
             * Called when the user stops touching the seek bar.
             *
             * @param seekBar The seek bar instance.
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * Updates the text view to display the number of training days.
     *
     * @param days The selected number of days.
     */
    private void updateTrainingDaysText(int days) {
        String text = days + " days per week";
        textViewTrainingDays.setText(text);
    }

    /**
     * Sets up the next button to pass the selected number of training days to the data listener.
     */
    private void setupNextButton() {
        buttonNext.setOnClickListener(v -> dataListener.onDataCollected("trainingDays", selectedDays));
    }
}
