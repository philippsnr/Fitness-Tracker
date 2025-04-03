package com.example.fitnesstracker.ui.onboarding;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

import android.view.View;
import android.widget.SeekBar;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.ui.MainActivity;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class OnboardingActivityTest {

    @Before
    public void setUp() {

        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testCompleteOnboarding_navigatesToMainActivity() throws InterruptedException {

        ActivityScenario<OnboardingActivity> scenario = ActivityScenario.launch(OnboardingActivity.class);


        onView(allOf(withId(R.id.buttonStart), isCompletelyDisplayed()))
                .check(matches(isCompletelyDisplayed()))
                .perform(click());
        Thread.sleep(500);


        for (int i = 1; i < 7; i++) {
            onView(allOf(withId(R.id.buttonNext), isCompletelyDisplayed()))
                    .check(matches(isCompletelyDisplayed()));
            enterText(i);
            onView(allOf(withId(R.id.buttonNext), isCompletelyDisplayed()))
                    .perform(click());
            Thread.sleep(500);
        }


        intended(hasComponent(MainActivity.class.getName()));
    }
    public void enterText(int page) throws InterruptedException {
        switch(page) {
            case 1:

                onView(withId(R.id.editTextName))
                        .perform(typeText("Test Name"), closeSoftKeyboard());
                break;
            case 2:

                onView(withId(R.id.editTextWeight))
                        .perform(typeText("75"), closeSoftKeyboard());
                break;
            case 3:
                onView(withId(R.id.editTextHeight))
                        .perform(typeText("120"), closeSoftKeyboard());
                break;
            case 4:

                onView(withId(R.id.buttonSelectDate))
                        .perform(click());
               selectDate();
                break;
            case 5:
                onView(withId(R.id.editTextKfa))
                        .perform(typeText("15"), closeSoftKeyboard());
                break;
            case 6:
                onView(withId(R.id.radioLoseWeight))
                        .perform(click());
                onView(withId(R.id.buttonNext))
                        .perform(click());
            case 7:

                onView(withId(R.id.seekBarTrainingDays))
                        .perform(setSeekBarProgress(5));
                break;
            default:
                break;
        }
    }

    public static ViewAction setSeekBarProgress(final int progress) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.allOf(withId(R.id.seekBarTrainingDays), Matchers.instanceOf(SeekBar.class));
            }
            @Override
            public void perform(UiController uiController, View view) {
            }
            @Override
            public String getDescription() {
                return "Set progress on SeekBar to " + progress;
            }

        };
    }
    public void selectDate() {
        onView(withText("OK")).perform(click());
        onView(withId(R.id.buttonSelectDate))
                .check(matches(not(withText("Geburtsdatum auswählen")))); // Sicherstellen, dass das Datum sich geändert hat
    }

}
