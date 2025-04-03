package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.ExerciseSet;
import com.example.fitnesstracker.repository.ExerciseSetRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExerciseSetViewModelTest {
    private ExerciseSetViewModel viewModel;
    private ExerciseSetRepository repository;
    private Context context;
    private DatabaseHelper dbHelper;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);
        repository = new ExerciseSetRepository(context);
        viewModel = new ExerciseSetViewModel((Application) context);

        clearDatabase();

        LocalDate today = LocalDate.now();
        ExerciseSet testSet1 = new ExerciseSet(1, 1, 10, 50.0, today.minusDays(1).toString());
        ExerciseSet testSet2 = new ExerciseSet(1, 2, 8, 55.0, today.toString());
        ExerciseSet testSet3 = new ExerciseSet(2, 1, 12, 30.0, today.minusWeeks(1).toString());

        repository.saveNewSet(testSet1);
        repository.saveNewSet(testSet2);
        repository.saveNewSet(testSet3);
    }

    @After
    public void tearDown() {
        clearDatabase();
        dbHelper.close();
    }

    private void clearDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("ExerciseSet", null, null);
        db.close();
    }

    @Test
    public void testLoadLastSets() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final List<ExerciseSet>[] result = new List[1];

        viewModel.loadLastSets(1, sets -> {
            result[0] = sets;
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertNotNull("Daten sollten geladen werden", result[0]);
        assertEquals("Es sollten 2 Sätze vorhanden sein", 2, result[0].size());

        assertEquals("Neuester Satz sollte Setnummer 2 haben", 2, result[0].get(0).getSetNumber());
        assertEquals("Älterer Satz sollte Setnummer 1 haben", 1, result[0].get(1).getSetNumber());
    }

    @Test
    public void testLoadSetsPerWeek() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final Map<Integer, Integer>[] result = new Map[1];

        LocalDate fixedDate = LocalDate.of(2024, 5, 15);
        ExerciseSet fixedSet1 = new ExerciseSet(3, 1, 10, 50.0, fixedDate.toString());
        ExerciseSet fixedSet2 = new ExerciseSet(3, 2, 8, 55.0, fixedDate.toString());
        ExerciseSet fixedSet3 = new ExerciseSet(4, 1, 12, 30.0, fixedDate.minusWeeks(1).toString());

        repository.saveNewSet(fixedSet1);
        repository.saveNewSet(fixedSet2);
        repository.saveNewSet(fixedSet3);

        viewModel.loadSetsPerWeek(setsPerWeek -> {
            result[0] = setsPerWeek;
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);

        assertNotNull(result[0]);
        assertFalse(result[0].isEmpty());

        int sqlWeekCurrent = 20;
        int sqlWeekPrevious = 19;

        assertTrue("KW20 sollte existieren", result[0].containsKey(sqlWeekCurrent));
        assertTrue("KW19 sollte existieren", result[0].containsKey(sqlWeekPrevious));

        assertEquals("KW20 sollte 2 Sätze haben", 2, (int) result[0].get(sqlWeekCurrent));
        assertEquals("KW19 sollte 1 Satz haben", 1, (int) result[0].get(sqlWeekPrevious));
    }

    @Test
    public void testGetLastSetNumber() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final int[] result = new int[1];

        String currentDate = LocalDate.now().toString();

        viewModel.getLastSetNumber(currentDate, 1, new ExerciseSetViewModel.OnLastSetNumberLoadedListener() {
            @Override
            public void onLastSetNumberLoaded(int lastSetNumber) {
                result[0] = lastSetNumber;
                latch.countDown();
            }
        });

        latch.await(2, TimeUnit.SECONDS);

        assertEquals(2, result[0]);
    }

    @Test
    public void testSaveNewSet() throws InterruptedException {
        ExerciseSet newSet = new ExerciseSet(3, 1, 12, 40.0, LocalDate.now().toString());

        viewModel.saveNewSet(newSet);


        Thread.sleep(500);

        List<ExerciseSet> sets = repository.getLastSets(3);
        assertEquals(1, sets.size());
        assertEquals(12, sets.get(0).getRepetition());
    }
}