package com.example.fitnesstracker.ui.onboarding;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.fitnesstracker.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OnboardingBirthdayFragment extends Fragment {
    private MaterialButton buttonSelectDate;
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
        View root = inflater.inflate(R.layout.fragment_onboarding_birthday, container, false);

        initializeViews(root);
        setupDatePicker();
        setupNextButton();

        return root;
    }

    private void initializeViews(View root) {
        buttonSelectDate = root.findViewById(R.id.buttonSelectDate);
        buttonNext = root.findViewById(R.id.buttonNext);
    }

    private void setupDatePicker() {
        buttonSelectDate.setFocusable(false);
        buttonSelectDate.setClickable(true);
        buttonSelectDate.setOnClickListener(v -> showMaterialDatePicker());
    }

    private void showMaterialDatePicker() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointBackward.now()); // Keine zukünftigen Daten

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Wähle dein Geburtsdatum")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String formattedDate = sdf.format(new Date(selection));
            buttonSelectDate.setText(formattedDate);
        });

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }

    private void setupNextButton() {
        buttonNext.setOnClickListener(v -> handleNextButtonClick());
    }

    private void handleNextButtonClick() {
        String birthdayStr = buttonSelectDate.getText().toString().trim();
        if (birthdayStr.isEmpty() || birthdayStr.equals("Geburtsdatum auswählen")) {
            Toast.makeText(getContext(), "Please select your birth year", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            dataListener.onDataCollected("birthday", birthdayStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid birth year", Toast.LENGTH_SHORT).show();
        }
    }

}
