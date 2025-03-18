package com.example.fitnesstracker.ui.onboarding;

import android.app.AlertDialog;
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

public class OnboardingKfaFragment extends Fragment {
    private EditText editTextKfa;
    private Button buttonShowPopup;
    private Button buttonFinish;
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
        View root = inflater.inflate(R.layout.fragment_onboarding_kfa, container, false);
        ImageView imageView = root.findViewById(R.id.imageViewPlaceholder);
        imageView.setImageResource(R.drawable.placeholder_image);

        editTextKfa = root.findViewById(R.id.editTextKfa);
        buttonShowPopup = root.findViewById(R.id.buttonShowPopup);
        buttonFinish = root.findViewById(R.id.buttonFinish);

        buttonShowPopup.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("KFA Help");
            ImageView popupImage = new ImageView(getContext());
            popupImage.setImageResource(R.drawable.placeholder_image);
            builder.setView(popupImage);
            builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
            builder.show();
        });

        buttonFinish.setOnClickListener(v -> {
            String kfaStr = editTextKfa.getText().toString().trim();
            if (TextUtils.isEmpty(kfaStr)) {
                Toast.makeText(getContext(), "Please enter your body fat percentage", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int kfa = Integer.parseInt(kfaStr);
                    dataListener.onDataCollected("kfa", kfa);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid format", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }
}
