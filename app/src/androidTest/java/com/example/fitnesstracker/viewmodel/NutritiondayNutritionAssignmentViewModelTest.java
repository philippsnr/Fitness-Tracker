package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.model.Nutritionday;
import com.example.fitnesstracker.model.NutritiondayNutritionAssignment;
import com.example.fitnesstracker.viewmodel.NutritiondayNutritionAssignmentViewModel;
import com.example.fitnesstracker.viewmodel.NutritiondayViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NutritiondayNutritionAssignmentViewModelTest {

    @Test
    public void testNutritiondayNutritionAssignmentSaveAndLoad() throws InterruptedException {
        Application app = ApplicationProvider.getApplicationContext();

        // Zuerst: Erstelle einen Nutritionday (Foreign Key)
        NutritiondayViewModel nutritiondayViewModel = new NutritiondayViewModel(app);
        String testDate = "02-02-2025";
        Nutritionday nutritionday = new Nutritionday(testDate);
        nutritiondayViewModel.saveNutritionday(nutritionday);

        CountDownLatch latchDay = new CountDownLatch(1);
        final Nutritionday[] loadedNutritionday = new Nutritionday[1];
        nutritiondayViewModel.loadNutritionday(testDate, new NutritiondayViewModel.Callback<Nutritionday>() {
            @Override
            public void onComplete(Nutritionday result) {
                loadedNutritionday[0] = result;
                latchDay.countDown();
            }
        });
        assertTrue("Timed out waiting for Nutritionday load", latchDay.await(5, TimeUnit.SECONDS));
        assertNotNull("Loaded Nutritionday should not be null", loadedNutritionday[0]);

        // Speichere ein NutritiondayNutritionAssignment
        int nutritiondayId = loadedNutritionday[0].getId();
        NutritiondayNutritionAssignment assignment = new NutritiondayNutritionAssignment(
                0, // ID wird von der Datenbank vergeben
                nutritiondayId,
                "12:00",
                "Chicken Salad",
                200, 350, 10, 15, 30
        );
        assignment.setNutritionNameGerman("Hähnchensalat");
        assignment.setNutritionPicturePath("/path/to/picture.jpg");

        NutritiondayNutritionAssignmentViewModel assignmentViewModel = new NutritiondayNutritionAssignmentViewModel(app);
        assignmentViewModel.saveNutritiondayNutritionAssignment(assignment);

        // Laden des gespeicherten NutritiondayNutritionAssignment
        CountDownLatch latchAssignment = new CountDownLatch(1);
        final NutritiondayNutritionAssignment[] loadedAssignment = new NutritiondayNutritionAssignment[1];
        assignmentViewModel.loadNutritiondayNutritionAssignment(nutritiondayId, new NutritiondayNutritionAssignmentViewModel.Callback<NutritiondayNutritionAssignment>() {
            @Override
            public void onComplete(NutritiondayNutritionAssignment result) {
                loadedAssignment[0] = result;
                latchAssignment.countDown();
            }
        });
        assertTrue("Timed out waiting for NutritiondayNutritionAssignment load", latchAssignment.await(5, TimeUnit.SECONDS));

        // Überprüfe alle Felder
        assertNotNull("Loaded assignment should not be null", loadedAssignment[0]);
        assertEquals("English Name must match", "Chicken Salad", loadedAssignment[0].getNutritionNameEnglish());
        assertEquals("German Name must match", "Hähnchensalat", loadedAssignment[0].getNutritionNameGerman());
        assertEquals("Picture path must match", "/path/to/picture.jpg", loadedAssignment[0].getNutritionPicturePath());
        assertEquals("Nutrition mass must match", 200, loadedAssignment[0].getNutritionMass());
        assertEquals("Nutrition cals must match", 350, loadedAssignment[0].getNutritionCals());
        assertEquals("Nutrition carbs must match", 10, loadedAssignment[0].getNutritionCarbs());
        assertEquals("Nutrition fats must match", 15, loadedAssignment[0].getNutritionFats());
        assertEquals("Nutrition proteins must match", 30, loadedAssignment[0].getNutritionProteins());
    }
}
