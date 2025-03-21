package com.example.fitnesstracker.ui.onboarding;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.fitnesstracker.R;
import java.util.Calendar;

public class OnboardingBirthYearFragment extends Fragment {
    private EditText editTextBirthYear;
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
        View root = inflater.inflate(R.layout.fragment_onboarding_birthyear, container, false);

        initializeViews(root);
        setupDatePicker();
        setupNextButton();

        return root;
    }

    private void initializeViews(View root) {
        ImageView imageView = root.findViewById(R.id.imageViewPlaceholder);
        imageView.setImageResource(R.drawable.placeholder_image);

        editTextBirthYear = root.findViewById(R.id.editTextBirthYear);
        buttonNext = root.findViewById(R.id.buttonNext);
    }

    private void setupDatePicker() {
        editTextBirthYear.setFocusable(false);
        editTextBirthYear.setClickable(true);
        editTextBirthYear.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            editTextBirthYear.setText(String.valueOf(selectedYear));
        }, year, month, day);

        datePickerDialog.show();
    }

    private void setupNextButton() {
        buttonNext.setOnClickListener(v -> handleNextButtonClick());
    }

    private void handleNextButtonClick() {
        String birthYearStr = editTextBirthYear.getText().toString().trim();
        if (birthYearStr.isEmpty()) {
            Toast.makeText(getContext(), "Please select your birth year", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int birthYear = Integer.parseInt(birthYearStr);
            dataListener.onDataCollected("birthYear", birthYearStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid birth year", Toast.LENGTH_SHORT).show();
        }
    }

}
