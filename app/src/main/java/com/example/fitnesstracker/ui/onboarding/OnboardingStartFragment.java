package com.example.fitnesstracker.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.fitnesstracker.R;

/**
 * Fragment that serves as the start of the onboarding process.
 */
public class OnboardingStartFragment extends Fragment {
    private OnboardingDataListener dataListener;

    /**
     * Attaches the fragment to the context and ensures that it implements OnboardingDataListener.
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
     * Inflates the layout and initializes the start button, setting up its click listener.
     *
     * @param inflater LayoutInflater to inflate the view.
     * @param container The parent container.
     * @param savedInstanceState Previously saved state.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_onboarding_start, container, false);
        Button buttonStart = root.findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(v -> dataListener.onDataCollected("name", null));
        return root;
    }
}
