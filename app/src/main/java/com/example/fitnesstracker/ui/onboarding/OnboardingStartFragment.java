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

public class OnboardingStartFragment extends Fragment {
    private Button buttonStart;
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
        View root = inflater.inflate(R.layout.fragment_onboarding_start, container, false);
        buttonStart = root.findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(v -> {
            dataListener.onDataCollected("name", null);
        });
        return root;
    }
}
