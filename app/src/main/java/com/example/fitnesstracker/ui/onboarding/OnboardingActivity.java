package com.example.fitnesstracker.ui.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.User;
import com.example.fitnesstracker.model.UserInformation;
import com.example.fitnesstracker.repository.UserInformationRepository;
import com.example.fitnesstracker.repository.UserRepository;
import com.example.fitnesstracker.ui.MainActivity;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class OnboardingActivity extends AppCompatActivity implements OnboardingDataListener {

    private ViewPager2 viewPager;
    private OnboardingPagerAdapter pagerAdapter;

    // Gesammelte Daten
    private String userName;
    private double weight;
    private int height;
    private String birthday;
    private int kfa;
    private String userGoal;
    private int trainingDaysPerWeek;
    private UserInformationRepository userInformationRepository;
    private UserRepository userRepository;
    private final Map<String, Consumer<Object>> dataHandlers = new HashMap<>();

    private static final Map<Integer, Fragment> FRAGMENT_MAP = new HashMap<>();
    static {
        FRAGMENT_MAP.put(0, new OnboardingStartFragment());
        FRAGMENT_MAP.put(1, new OnboardingNameFragment());
        FRAGMENT_MAP.put(2, new OnboardingWeightFragment());
        FRAGMENT_MAP.put(3, new OnboardingHeightFragment());
        FRAGMENT_MAP.put(4, new OnboardingBirthdayFragment());
        FRAGMENT_MAP.put(5, new OnboardingKfaFragment());
        FRAGMENT_MAP.put(6, new OnboardingGoalFragment());
        FRAGMENT_MAP.put(7, new OnboardingTrainingDaysFragment());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        userInformationRepository = new UserInformationRepository(this);
        userRepository = new UserRepository(this);

        viewPager = findViewById(R.id.onboardingViewPager);
        pagerAdapter = new OnboardingPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        initializeDataHandlers();
    }

    private void initializeDataHandlers() {
        dataHandlers.put("name", data -> userName = (String) data);
        dataHandlers.put("weight", data -> weight = (Double) data);
        dataHandlers.put("height", data -> height = (Integer) data);
        dataHandlers.put("birthday", data -> birthday = (String) data);
        dataHandlers.put("kfa", data -> kfa = (Integer) data);
        dataHandlers.put("goal", data -> userGoal = (String) data);
        dataHandlers.put("trainingDays", data -> trainingDaysPerWeek = (Integer) data);
    }

    @Override
    public void onDataCollected(String key, Object data) {
        saveCollectedData(key, data);

        if (isLastOnboardingPage()) {
            completeOnboarding();
        } else {
            moveToNextPage();
        }
    }

    private void saveCollectedData(String key, Object data) {
        Consumer<Object> handler = dataHandlers.get(key);
        if (handler != null) {
            handler.accept(data);
        }
    }

    private boolean isLastOnboardingPage() {
        return viewPager.getCurrentItem() >= pagerAdapter.getItemCount() - 1;
    }

    private void moveToNextPage() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    private void completeOnboarding() {
        saveUserData();
        saveOnboardingStatus();
        navigateToMainActivity();
    }

    private void saveUserData() {
        UserInformation userInfo = new UserInformation(0, 1, new Date(), height, weight, kfa);
        User user = new User(1, userName, birthday, userGoal, trainingDaysPerWeek);
        userInformationRepository.writeUserInformation(userInfo);
        userRepository.saveUser(user);

    }

    private void saveOnboardingStatus() {
        SharedPreferences prefs = getSharedPreferences("onboarding", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("onboarding_complete", true);
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Adapter für ViewPager2
    private static class OnboardingPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {

        public OnboardingPagerAdapter(@NonNull AppCompatActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public androidx.fragment.app.Fragment createFragment(int position) {
            return Objects.requireNonNull(FRAGMENT_MAP.getOrDefault(position, new OnboardingNameFragment()));
        }

        @Override
        public int getItemCount() {
            return 8; // Anzahl der Fragmente muss mit createFragment() übereinstimmen
        }
    }
}
