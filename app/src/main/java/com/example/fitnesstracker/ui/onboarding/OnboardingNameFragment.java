package com.example.fitnesstracker.ui.onboarding;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.fitnesstracker.R;

/**
 * Fragment for collecting the user's name during onboarding.
 */
public class OnboardingNameFragment extends Fragment {
    private EditText editTextName;
    private Button buttonNext;
    private OnboardingDataListener dataListener;

    /**
     * Attaches the fragment to the context and ensures that the context implements OnboardingDataListener.
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
     * Inflates the layout, initializes UI components, and sets up the button click listener.
     *
     * @param inflater LayoutInflater to inflate the view.
     * @param container The parent container.
     * @param savedInstanceState Previously saved state.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_onboarding_name, container, false);
        editTextName = root.findViewById(R.id.editTextName);
        buttonNext = root.findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(getContext(), "Bitte gebe deinen Namen ein.", Toast.LENGTH_SHORT).show();
            } else {
                dataListener.onDataCollected("name", name);
            }
        });

        return root;
    }
}
