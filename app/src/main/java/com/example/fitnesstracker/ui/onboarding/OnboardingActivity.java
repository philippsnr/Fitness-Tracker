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
        // Speichere die Daten je nach Schlüssel
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
            case "age":
                age = (Integer) data;
                break;
            case "kfa":
                kfa = (Integer) data;
                break;
        }

        // Wenn noch nicht die letzte Seite, gehe zur nächsten
        if (viewPager.getCurrentItem() < pagerAdapter.getItemCount() - 1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        } else {
            // Onboarding beenden: Speichere Daten und setze den Flag in SharedPreferences
            UserInformation userInfo = new UserInformation(0, 1, new Date(), height, weight, kfa);
            userInformationRepository.writeUserInformation(userInfo);

            SharedPreferences prefs = getSharedPreferences("onboarding", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("onboarding_complete", true);
            editor.putString("user_name", userName);
            editor.putInt("user_age", age);
            editor.apply();

            // Wechsle zur MainActivity
            Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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
                    return new OnboardingAgeFragment();
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
