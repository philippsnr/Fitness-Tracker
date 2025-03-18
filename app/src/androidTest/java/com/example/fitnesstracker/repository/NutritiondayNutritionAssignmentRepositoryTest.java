package com.example.fitnesstracker.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.NutritiondayNutritionAssignment;
import com.example.fitnesstracker.repository.NutritiondayNutritionAssignmentRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NutritiondayNutritionAssignmentRepositoryTest {

    private NutritiondayNutritionAssignmentRepository repository;
    private Context appContext;
    private DatabaseHelper dbHelper;


    @Before
    public void setUp() {
        // Application Context holen
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // DatabaseHelper initialisieren
        dbHelper = new DatabaseHelper(appContext);
        // Repository initialisieren
        repository = new NutritiondayNutritionAssignmentRepository(appContext);
        // Säubere die Tabelle vor jedem Test
        clearDatabaseTable();
    }

    @After
    public void tearDown() {
        // Säubere die Tabelle nach jedem Test
        clearDatabaseTable();
    }

    /**
     * Hilfsmethode, um die Tabelle "NutritiondayNutritionAssignment" zu leeren.
     */
    private void clearDatabaseTable() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("NutritiondayNutritionAssignment", null, null);
        db.close();
    }


    @Test
    public void testInsertAndRetrieveNutritiondayNutritionAssignment() {
        // Test-Daten erstellen
        int testNutritiondayId = 80;
        NutritiondayNutritionAssignment testAssignment = new NutritiondayNutritionAssignment(
                500,
                testNutritiondayId,
                "08:00",
                "Oatmeal",
                250,
                150,
                30,
                5,
                10
        );
        testAssignment.setNutritionNameGerman("Haferbrei");
        testAssignment.setNutritionPicturePath("/images/oatmeal.png");

        // Datensatz einfügen
        repository.setNutritiondayNutritionAssignment(testAssignment);

        // Datensatz abrufen
        NutritiondayNutritionAssignment retrievedAssignment = repository.getNutritionday(testNutritiondayId);

        // Überprüfen, dass der abgerufene Datensatz nicht null ist
        assertNotNull("Der abgerufene Datensatz sollte nicht null sein", retrievedAssignment);

        // Überprüfen der einzelnen Felder
        assertEquals("Die nutritiondayId sollte übereinstimmen", testNutritiondayId, retrievedAssignment.getNutritiondayId());
        assertEquals("Die Zeit sollte übereinstimmen", "08:00", retrievedAssignment.getTime());
        assertEquals("Der englische Name sollte übereinstimmen", "Oatmeal", retrievedAssignment.getNutritionNameEnglish());
        assertEquals("Die Masse sollte übereinstimmen", 250, retrievedAssignment.getNutritionMass());
        assertEquals("Die Kalorien sollten übereinstimmen", 150, retrievedAssignment.getNutritionCals());
        assertEquals("Die Kohlenhydrate sollten übereinstimmen", 30, retrievedAssignment.getNutritionCarbs());
        assertEquals("Die Fette sollten übereinstimmen", 5, retrievedAssignment.getNutritionFats());
        assertEquals("Die Proteine sollten übereinstimmen", 10, retrievedAssignment.getNutritionProteins());
        assertEquals("Der deutsche Name sollte übereinstimmen", "Haferbrei", retrievedAssignment.getNutritionNameGerman());
        assertEquals("Der Bildpfad sollte übereinstimmen", "/images/oatmeal.png", retrievedAssignment.getNutritionPicturePath());
    }

    @Test
    public void testRetrieveNonExistentAssignmentReturnsNull() {
        // Versuche, einen Datensatz mit einer nicht existierenden nutritiondayId abzurufen
        NutritiondayNutritionAssignment assignment = repository.getNutritionday(9999);
        assertNull("Beim Abruf eines nicht existierenden Datensatzes sollte null zurückgegeben werden", assignment);
    }
}
