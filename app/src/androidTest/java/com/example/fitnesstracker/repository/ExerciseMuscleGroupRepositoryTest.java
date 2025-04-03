package com.example.fitnesstracker.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.fitnesstracker.database.DatabaseHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExerciseMuscleGroupRepositoryTest {
    private ExerciseMuscleGroupRepository repository;
    private DatabaseHelper dbHelper;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM ExerciseMuscleGroupAssignment");

        db.execSQL("INSERT INTO ExerciseMuscleGroupAssignment (Exercise_id, MuscleGroup_id) " +
                "VALUES (1, 3), (1, 5), (2, 3)");

        repository = new ExerciseMuscleGroupRepository(context);
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testGetMuscleGroupsForExercise() {
        List<Integer> result = repository.getMuscleGroupsForExercise(1);
        assertEquals(2, result.size());
        assertTrue(result.contains(3));
        assertTrue(result.contains(5));
    }

    @Test
    public void testGetMuscleGroupsForNonExistingExercise() {
        List<Integer> result = repository.getMuscleGroupsForExercise(99);
        assertTrue(result.isEmpty());
    }
}