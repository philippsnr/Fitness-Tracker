package com.example.fitnesstracker.progression;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserRepositoryTest {
    private UserRepository userRepository;
    private DatabaseHelper dbHelper;
    private Context context;

    /** Setzt die Testumgebung auf, indem eine frische Datenbankinstanz erstellt wird. */
    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM User");
        db.execSQL("INSERT INTO User (id, name, birth_date, goal, trainingdaysPerWeek) VALUES (1, 'Test User', '1990-01-01', 'Abnehmen', 3)");
        db.close();
        userRepository = new UserRepository(context);
    }

    /** Schließt die Datenbank nach jedem Test. */
    @After
    public void tearDown() {
        dbHelper.close();
    }

    /** Testet, ob das Benutzerziel erfolgreich gespeichert und abgerufen werden kann. */
    @Test
    public void testUpdateAndGetUserGoal() {
        String expectedGoal = "Abnehmen";
        userRepository.updateUserGoal(expectedGoal);
        String actualGoal = userRepository.getUserGoal();
        assertEquals(expectedGoal, actualGoal);
    }
}
