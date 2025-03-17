package com.example.fitnesstracker.repository;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.NutritiondayNutritionAssignment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NutritiondayNutritionAssignmentRepositoryTest {

    private NutritiondayNutritionAssignmentRepository repository;
    private DatabaseHelper dbHelper;
    private static final int TEST_NUTRITIONDAY_ID = 100;
    private static final String TEST_TIME = "10:00";
    private static final String TEST_NAME_EN = "Oatmeal";
    private static final String TEST_NAME_DE = "Haferbrei";

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);

        // Clean and prepare database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM NutritiondayNutritionAssignment");
        db.execSQL("DELETE FROM Nutritionday");

        // Insert test nutritionday
        db.execSQL("INSERT INTO Nutritionday(id, date) VALUES (" + TEST_NUTRITIONDAY_ID + ", '2023-01-01')");

        repository = new NutritiondayNutritionAssignmentRepository(context);
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testSetAndGetNutritiondayAssignment() {
        // Create test object
        NutritiondayNutritionAssignment assignment = new NutritiondayNutritionAssignment(
                0,  // id will be auto-generated
                TEST_NUTRITIONDAY_ID,
                TEST_TIME,
                TEST_NAME_EN,
                100,  // mass
                350,  // cals
                50,   // carbs
                10,   // fats
                15    // proteins
        );
        assignment.setNutritionNameGerman(TEST_NAME_DE);
        assignment.setNutritionPicturePath("oatmeal.jpg");

        // Save to database
        repository.setNutritiondayNutritionAssignment(assignment);

        // Retrieve from database
        NutritiondayNutritionAssignment result = repository.getNutritionday(TEST_NUTRITIONDAY_ID);

        // Verify results
        assertNotNull(result);
        assertEquals(TEST_NUTRITIONDAY_ID, result.getNutritiondayId());
        assertEquals(TEST_TIME, result.getTime());
        assertEquals(TEST_NAME_EN, result.getNutritionNameEnglish());
        assertEquals(TEST_NAME_DE, result.getNutritionNameGerman());
        assertEquals("oatmeal.jpg", result.getNutritionPicturePath());
        assertEquals(100, result.getNutritionMass());
        assertEquals(350, result.getNutritionCals());
    }

    @Test
    public void testGetNonExistingNutritionday() {
        NutritiondayNutritionAssignment result = repository.getNutritionday(999);
        assertNull(result);
    }

    @Test
    public void testInsertWithOptionalFieldsNull() {
        NutritiondayNutritionAssignment assignment = new NutritiondayNutritionAssignment(
                0,
                TEST_NUTRITIONDAY_ID,
                TEST_TIME,
                TEST_NAME_EN,
                100,
                350,
                50,
                10,
                15
        );
        // Leave german name and picture path null

        repository.setNutritiondayNutritionAssignment(assignment);

        NutritiondayNutritionAssignment result = repository.getNutritionday(TEST_NUTRITIONDAY_ID);
        assertNotNull(result);
        assertNull(result.getNutritionNameGerman());
        assertNull(result.getNutritionPicturePath());
    }
}