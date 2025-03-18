package com.example.fitnesstracker.repository;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Nutritionday;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NutritiondayRepositoryTest {

    private NutritiondayRepository repository;
    private DatabaseHelper dbHelper;
    private static final String TEST_DATE = "2023-10-05";
    private static final String NON_EXISTENT_DATE = "1999-01-01";

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);

        // Clean database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM Nutritionday");
        db.close();

        repository = new NutritiondayRepository(context);
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testCreateAndRetrieveNutritionday() {
        // Create test object OHNE ID
        Nutritionday newDay = new Nutritionday(TEST_DATE);

        // Save to database
        repository.setNutritionday(newDay);

        // Retrieve from database
        Nutritionday retrievedDay = repository.getNutritionday(TEST_DATE);

        // Verify results
        assertNotNull(retrievedDay);
        assertEquals(TEST_DATE, retrievedDay.getDate());
        assertTrue("ID sollte > 0 sein", retrievedDay.getId() > 0); // Auto-generierte ID
    }

    @Test
    public void testMultipleInsertsGenerateUniqueIds() {
        Nutritionday first = new Nutritionday("2023-10-01");
        Nutritionday second = new Nutritionday("2023-10-02");

        repository.setNutritionday(first);
        repository.setNutritionday(second);

        Nutritionday firstRetrieved = repository.getNutritionday("2023-10-01");
        Nutritionday secondRetrieved = repository.getNutritionday("2023-10-02");

        assertNotEquals("IDs sollten unterschiedlich sein",
                firstRetrieved.getId(),
                secondRetrieved.getId());
    }

    @Test
    public void testDateUniqueness() {
        String duplicateDate = "2023-10-10";
        Nutritionday first = new Nutritionday(duplicateDate);
        Nutritionday second = new Nutritionday(duplicateDate);

        repository.setNutritionday(first);
        repository.setNutritionday(second);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM Nutritionday WHERE date = ?",
                new String[]{duplicateDate}
        );
        cursor.moveToFirst();
        assertEquals("Es sollten 2 Einträge existieren", 2, cursor.getInt(0));
        cursor.close();
        db.close();
    }
}