package com.example.fitnesstracker.ui.onboarding;

import com.example.fitnesstracker.ui.onboarding.OnboardingDataListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.UserInformation;
import com.example.fitnesstracker.repository.UserInformationRepository;
import com.example.fitnesstracker.ui.MainActivity;
import java.util.Date;


public class OnboardingActivity extends AppCompatActivity implements OnboardingDataListener {

    private ViewPager2 viewPager;
    private OnboardingPagerAdapter pagerAdapter;

    // Gesammelte Daten
    private String userName;
    private double weight;
    private int height;
    private int age;
    private int kfa;

    private UserInformationRepository userInformationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        userInformationRepository = new UserInformationRepository(this);

        viewPager = findViewById(R.id.onboardingViewPager);
        pagerAdapter = new OnboardingPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
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
        switch (key) {
            case "name":
                userName = (String) data;
                break;
            case "weight":
                weight = (Double) data;
                break;
            case "height":
                height = (Integer) data;
                break;
            case "birthYear":
                age = (Integer) data;
                break;
            case "kfa":
                kfa = (Integer) data;
                break;
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
        userInformationRepository.writeUserInformation(userInfo);
    }

    private void saveOnboardingStatus() {
        SharedPreferences prefs = getSharedPreferences("onboarding", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("onboarding_complete", true);
        editor.putString("user_name", userName);
        editor.putInt("user_age", age);
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    // Innerer Adapter für den ViewPager2
    private class OnboardingPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {

        public OnboardingPagerAdapter(@NonNull AppCompatActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public androidx.fragment.app.Fragment createFragment(int position) {
            switch(position) {
                case 0:
                    return new OnboardingNameFragment();
                case 1:
                    return new OnboardingWeightFragment();
                case 2:
                    return new OnboardingHeightFragment();
                case 3:
                    return new OnboardingBirthYearFragment();
                case 4:
                    return new OnboardingKfaFragment();
                default:
                    return new OnboardingNameFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}
