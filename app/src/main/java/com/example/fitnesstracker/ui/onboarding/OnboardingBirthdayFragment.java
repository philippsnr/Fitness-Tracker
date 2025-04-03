package com.example.fitnesstracker.ui.onboarding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.fitnesstracker.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Fragment for the onboarding process allowing the user to select their birthday.
 */
public class OnboardingBirthdayFragment extends Fragment {
    private MaterialButton buttonSelectDate;
    private Button buttonNext;
    private OnboardingDataListener dataListener;

    /**
     * Attaches the fragment to the context and ensures it implements OnboardingDataListener.
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
     * Inflates the layout, initializes views, and sets up the date picker and next button.
     *
     * @param inflater LayoutInflater to inflate the view.
     * @param container The parent container.
     * @param savedInstanceState Previously saved state.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_onboarding_birthday, container, false);
        initializeViews(root);
        setupDatePicker();
        setupNextButton();
        return root;
    }

    /**
     * Initializes the UI components.
     *
     * @param root The root view.
     */
    private void initializeViews(View root) {
        buttonSelectDate = root.findViewById(R.id.buttonSelectDate);
        buttonNext = root.findViewById(R.id.buttonNext);
    }

    /**
     * Configures the date picker for birthday selection.
     */
    private void setupDatePicker() {
        buttonSelectDate.setFocusable(false);
        buttonSelectDate.setClickable(true);
        buttonSelectDate.setOnClickListener(v -> showMaterialDatePicker());
    }

    /**
     * Displays the Material Date Picker for the user to select their birthday.
     */
    private void showMaterialDatePicker() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointBackward.now());
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

    /**
     * Sets up the next button to handle advancing in the onboarding process.
     */
    private void setupNextButton() {
        buttonNext.setOnClickListener(v -> handleNextButtonClick());
    }

    /**
     * Handles the next button click by validating the birthday selection and notifying the listener.
     */
    private void handleNextButtonClick() {
        String birthdayStr = buttonSelectDate.getText().toString().trim();
        if (birthdayStr.isEmpty() || birthdayStr.equals("Geburtsdatum auswählen")) {
            Toast.makeText(getContext(), "Bitte wähle dein Geburtsdatum aus.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            dataListener.onDataCollected("birthday", birthdayStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Ungültige Eingabe.", Toast.LENGTH_SHORT).show();
        }
    }
}
