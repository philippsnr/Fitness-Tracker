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

public class OnboardingAgeFragment extends Fragment {
    private EditText editTextAge;
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
        View root = inflater.inflate(R.layout.fragment_onboarding_age, container, false);
        ImageView imageView = root.findViewById(R.id.imageViewPlaceholder);
        imageView.setImageResource(R.drawable.placeholder_image);

        editTextAge = root.findViewById(R.id.editTextAge);
        buttonNext = root.findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(v -> {
            String ageStr = editTextAge.getText().toString().trim();
            if (TextUtils.isEmpty(ageStr)) {
                Toast.makeText(getContext(), "Please enter your age", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int age = Integer.parseInt(ageStr);
                    dataListener.onDataCollected("age", age);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid age format", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }
}
