package com.example.fitnesstracker.ui.onboarding;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.fitnesstracker.R;

/**
 * Fragment for collecting the user's height during onboarding.
 */
public class OnboardingHeightFragment extends Fragment {
    private EditText editTextHeight;
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
     * Inflates the layout, initializes the UI components, and sets up the button click listener.
     *
     * @param inflater LayoutInflater to inflate the view.
     * @param container The parent container.
     * @param savedInstanceState Previously saved state.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_onboarding_height, container, false);
        editTextHeight = root.findViewById(R.id.editTextHeight);
        Button buttonNext = root.findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(v -> {
            String heightStr = editTextHeight.getText().toString().trim();
            if (TextUtils.isEmpty(heightStr)) {
                Toast.makeText(getContext(), "Bitte geb deine Größe ein.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int height = Integer.parseInt(heightStr);
                    dataListener.onDataCollected("height", height);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Ungültige Größeneingabe", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }
}
