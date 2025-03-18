package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class UserViewModelTest {

    /** Testet das Aktualisieren und Abrufen des Ziels "Abnehmen". */
    @Test
    public void testUserGoalAbnehmen() throws InterruptedException {
        Application application = ApplicationProvider.getApplicationContext();
        UserViewModel viewModel = new UserViewModel(application);
        final String expectedGoal = "Abnehmen";
        viewModel.updateUserGoal(expectedGoal);
        CountDownLatch latch = new CountDownLatch(1);
        viewModel.getUserGoal(new UserViewModel.OnGoalLoadedListener() {
            @Override
            public void onGoalLoaded(String goal) {
                try {
                    assertEquals(expectedGoal, goal);
                } finally {
                    latch.countDown();
                }
            }
        });
        boolean callbackCalled = latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackCalled);
    }

    /** Testet das Aktualisieren und Abrufen des Ziels "Gewicht halten". */
    @Test
    public void testUserGoalGewichtHalten() throws InterruptedException {
        Application application = ApplicationProvider.getApplicationContext();
        UserViewModel viewModel = new UserViewModel(application);
        final String expectedGoal = "Gewicht halten";
        viewModel.updateUserGoal(expectedGoal);
        CountDownLatch latch = new CountDownLatch(1);
        viewModel.getUserGoal(new UserViewModel.OnGoalLoadedListener() {
            @Override
            public void onGoalLoaded(String goal) {
                try {
                    assertEquals(expectedGoal, goal);
                } finally {
                    latch.countDown();
                }
            }
        });
        boolean callbackCalled = latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackCalled);
    }

    /** Testet das Aktualisieren und Abrufen des Ziels "Zunehmen". */
    @Test
    public void testUserGoalZunehmen() throws InterruptedException {
        Application application = ApplicationProvider.getApplicationContext();
        UserViewModel viewModel = new UserViewModel(application);
        final String expectedGoal = "Zunehmen";
        viewModel.updateUserGoal(expectedGoal);
        CountDownLatch latch = new CountDownLatch(1);
        viewModel.getUserGoal(new UserViewModel.OnGoalLoadedListener() {
            @Override
            public void onGoalLoaded(String goal) {
                try {
                    assertEquals(expectedGoal, goal);
                } finally {
                    latch.countDown();
                }
            }
        });
        boolean callbackCalled = latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackCalled);
    }
}
