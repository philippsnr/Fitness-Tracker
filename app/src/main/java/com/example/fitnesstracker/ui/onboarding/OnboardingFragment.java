package com.example.fitnesstracker.ui.onboarding;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.UserInformation;
import com.example.fitnesstracker.repository.UserInformationRepository;
import com.example.fitnesstracker.ui.progress.ProgressionFragment;
import java.util.Date;

public class OnboardingFragment extends Fragment {
    private EditText editTextName, editTextWeight, editTextHeight, editTextAge, editTextKfa;
    private Button buttonSubmit;
    private UserInformationRepository userInformationRepository;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_onboarding, container, false);

        editTextName = root.findViewById(R.id.editTextName);
        editTextWeight = root.findViewById(R.id.editTextWeight);
        editTextHeight = root.findViewById(R.id.editTextHeight);
        editTextAge = root.findViewById(R.id.editTextAge);
        editTextKfa = root.findViewById(R.id.editTextKfa);
        buttonSubmit = root.findViewById(R.id.buttonSubmit);

        userInformationRepository = new UserInformationRepository(getContext());

        buttonSubmit.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String weightStr = editTextWeight.getText().toString().trim();
            String heightStr = editTextHeight.getText().toString().trim();
            String ageStr = editTextAge.getText().toString().trim();
            String kfaStr = editTextKfa.getText().toString().trim();

            if (name.isEmpty() || weightStr.isEmpty() || heightStr.isEmpty() || ageStr.isEmpty() || kfaStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double weight;
            int height, age, kfa;
            try {
                weight = Double.parseDouble(weightStr);
                height = Integer.parseInt(heightStr);
                age = Integer.parseInt(ageStr);
                kfa = Integer.parseInt(kfaStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid input format", Toast.LENGTH_SHORT).show();
                return;
            }

            // Erstelle ein neues UserInformation-Objekt
            // Hier nehmen wir an, dass user_id 1 ist und die ID auto-generiert wird (wir setzen als Platzhalter 0)
            UserInformation userInfo = new UserInformation(0, 1, new Date(), height, weight, kfa);
            userInformationRepository.writeUserInformation(userInfo);

            // Speichere den Onboarding-Status und ggf. weitere Daten in SharedPreferences
            SharedPreferences prefs = getActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("onboarding_complete", true);
            editor.putString("user_name", name);
            editor.putInt("user_age", age);
            editor.apply();

            // Navigiere zur Haupt-App, hier beispielhaft zum ProgressionFragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProgressionFragment())
                    .commit();
        });

        return root;
    }
}

