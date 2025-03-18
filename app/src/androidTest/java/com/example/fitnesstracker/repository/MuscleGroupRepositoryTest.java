package com.example.fitnesstracker.repository;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.MuscleGroup;
import android.database.sqlite.SQLiteDatabase;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MuscleGroupRepositoryTest {

    private MuscleGroupRepository repository;

    private DatabaseHelper dbHelper;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Tabelle leeren
        db.execSQL("DELETE FROM MuscleGroup");

        // Testdaten einfügen
        db.execSQL("INSERT INTO MuscleGroup (id, name, picture_path) VALUES (1, 'Chest', 'path1'), (2, 'Back', 'path2')");

        repository = new MuscleGroupRepository(context);
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }
    @Test
    public void testGetAllMuscleGroups() {
        Context context = ApplicationProvider.getApplicationContext();
        repository = new MuscleGroupRepository(context);
        List<MuscleGroup> groups = repository.getAllMuscleGroups();
        assertFalse(groups.isEmpty());
        assertEquals(2, groups.size());
    }
}
