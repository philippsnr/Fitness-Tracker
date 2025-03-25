package com.example.fitnesstracker.ui.onboarding;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.fitnesstracker.R;

public class OnboardingKfaFragment extends Fragment {
    private EditText editTextKfa;
    private Button buttonShowPopup;
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
        View root = inflater.inflate(R.layout.fragment_onboarding_kfa, container, false);

        editTextKfa = root.findViewById(R.id.editTextKfa);
        buttonShowPopup = root.findViewById(R.id.buttonShowPopup);
        buttonNext = root.findViewById(R.id.buttonNext);

        buttonShowPopup.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            // Erstelle einen benutzerdefinierten weißen Titel
            TextView title = new TextView(getContext());
            title.setText("KFA Hilfe");
            title.setPadding(20, 20, 20, 20);
            title.setTextSize(20);
            title.setTextColor(Color.WHITE);
            title.setTypeface(null, Typeface.BOLD);
            title.setGravity(Gravity.CENTER);

            builder.setCustomTitle(title);

            ImageView popupImage = new ImageView(getContext());
            popupImage.setImageResource(R.drawable.kfa_help);
            builder.setView(popupImage);

            builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        });


        buttonNext.setOnClickListener(v -> {
            String kfaStr = editTextKfa.getText().toString().trim();
            if (TextUtils.isEmpty(kfaStr)) {
                Toast.makeText(getContext(), "Bitte gebe deinen KFA an.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int kfa = Integer.parseInt(kfaStr);
                    dataListener.onDataCollected("kfa", kfa);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Ungültige KFA-Eingabe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }
}
