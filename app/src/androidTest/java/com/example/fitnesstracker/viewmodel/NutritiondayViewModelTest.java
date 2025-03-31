package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.model.Nutritionday;
import com.example.fitnesstracker.viewmodel.NutritiondayViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NutritiondayViewModelTest {

    @Test
    public void testNutritiondaySaveAndLoad() throws InterruptedException {
        Application app = ApplicationProvider.getApplicationContext();
        NutritiondayViewModel viewModel = new NutritiondayViewModel(app);

        // Erstelle ein Nutritionday-Objekt mit Testdatum
        String testDate = "01-01-2025";
        Nutritionday nutritionday = new Nutritionday(testDate);

        // Speichern
        viewModel.saveNutritionday(nutritionday);

        // Laden des Objekts (asynchron)
        CountDownLatch latch = new CountDownLatch(1);
        final Nutritionday[] loadedNutritionday = new Nutritionday[1];
        viewModel.loadNutritionday(testDate, new NutritiondayViewModel.Callback<Nutritionday>() {
            @Override
            public void onComplete(Nutritionday result) {
                loadedNutritionday[0] = result;
                latch.countDown();
            }
        });

        // Warte maximal 5 Sekunden
        assertTrue("Timed out waiting for Nutritionday load", latch.await(5, TimeUnit.SECONDS));

        // Prüfe, ob das geladene Objekt existiert und das Datum korrekt ist
        assertNotNull("Loaded Nutritionday should not be null", loadedNutritionday[0]);
        assertEquals("Das Datum muss übereinstimmen", testDate, loadedNutritionday[0].getDate());
    }
}
