package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.fitnesstracker.model.UserInformation;

@RunWith(AndroidJUnit4.class)
public class UserInformationViewModelTest {

    /** Testet die Berechnung des BMI aus dem neuesten Eintrag der Benutzerinformationen. */
    @Test
    public void testGetBMI() throws InterruptedException {
        Application application = ApplicationProvider.getApplicationContext();
        UserInformationViewModel viewModel = new UserInformationViewModel(application);
        Date now = new Date();
        UserInformation userInfo = new UserInformation(0, 1, now, 180, 81.0, 15);
        viewModel.writeUserInformation(userInfo);
        CountDownLatch latch = new CountDownLatch(1);
        final double[] bmiResult = new double[1];
        viewModel.getBMI(bmi -> {
            bmiResult[0] = bmi;
            latch.countDown();
        });
        boolean callbackCalled = latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackCalled);
        assertEquals(25.0, bmiResult[0], 0.1);
    }

    /** Testet das Abrufen aller Benutzerinformationen in aufsteigender Reihenfolge nach Datum. */
    @Test
    public void testGetAllUserInformation() throws InterruptedException {
        Application application = ApplicationProvider.getApplicationContext();
        UserInformationViewModel viewModel = new UserInformationViewModel(application);
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, -1);
        Date date1 = calendar.getTime();
        calendar.add(Calendar.MINUTE, 2);
        Date date2 = calendar.getTime();
        UserInformation info1 = new UserInformation(0, 1, date1, 175, 70.0, 20);
        viewModel.writeUserInformation(info1);
        UserInformation info2 = new UserInformation(0, 1, date2, 175, 75.0, 22);
        viewModel.writeUserInformation(info2);
        CountDownLatch latch = new CountDownLatch(1);
        final List<UserInformation>[] dataList = new List[1];
        viewModel.getAllUserInformation(list -> {
            dataList[0] = list;
            latch.countDown();
        });
        boolean callbackCalled = latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackCalled);
        assertTrue(dataList[0].size() >= 2);
        assertTrue(dataList[0].get(0).getDate().before(dataList[0].get(1).getDate()));
    }

    /** Testet das Abrufen des neuesten Eintrags der Benutzerinformationen. */
    @Test
    public void testGetLatestUserInformation() throws InterruptedException {
        Application application = ApplicationProvider.getApplicationContext();
        UserInformationViewModel viewModel = new UserInformationViewModel(application);
        Date now = new Date();
        UserInformation info = new UserInformation(0, 1, now, 170, 65.0, 18);
        viewModel.writeUserInformation(info);
        CountDownLatch latch = new CountDownLatch(1);
        final UserInformation[] latestInfo = new UserInformation[1];
        viewModel.getLastUserInformation(data -> {
            latestInfo[0] = data;
            latch.countDown();
        });
        boolean callbackCalled = latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackCalled);
        assertEquals(170, latestInfo[0].getHeight());
        assertEquals(65.0, latestInfo[0].getWeight(), 0.1);
        assertEquals(18, latestInfo[0].getKfa());
    }
}
