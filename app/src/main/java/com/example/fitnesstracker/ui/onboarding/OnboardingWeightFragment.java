package com.example.fitnesstracker.ui.onboarding;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.fitnesstracker.R;

public class OnboardingWeightFragment extends Fragment {
    private EditText editTextWeight;
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
        View root = inflater.inflate(R.layout.fragment_onboarding_weight, container, false);

        editTextWeight = root.findViewById(R.id.editTextWeight);
        buttonNext = root.findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(v -> {
            String weightStr = editTextWeight.getText().toString().trim();
            if (TextUtils.isEmpty(weightStr)) {
                Toast.makeText(getContext(), "Bitte gebe dein Gewicht ein", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    double weight = Double.parseDouble(weightStr);
                    dataListener.onDataCollected("weight", weight);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Ungültige Gewichtseingabe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }
}
