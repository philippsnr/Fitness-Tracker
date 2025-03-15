package com.example.fitnesstracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.app.Application;
import android.os.Bundle;
import android.widget.TextView;
import androidx.test.core.app.ApplicationProvider;

import androidx.fragment.app.testing.FragmentScenario;
import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.ui.training.TrainingExerciseAdapter;
import com.example.fitnesstracker.ui.training.TrainingExerciseFragment;
import com.example.fitnesstracker.viewmodel.ExerciseViewModel;
import com.example.fitnesstracker.viewmodel.TrainingdayExerciseAssignmentViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@RunWith(RobolectricTestRunner.class)
public class TrainingExerciseFragmentTest {

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @SuppressWarnings("unchecked")
    private <T> T getField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(target);
    }

    @Test
    public void testFragmentTitleIsSet() throws Exception {
        TrainingExerciseFragment fragment = TrainingExerciseFragment.newInstance(1, "Test Training");

        Application application = ApplicationProvider.getApplicationContext(); // Application holen
        FakeExerciseViewModel fakeExerciseVM = new FakeExerciseViewModel(application); // Application übergeben
        FakeAssignmentViewModel fakeAssignmentVM = new FakeAssignmentViewModel(application);

        fakeAssignmentVM.setExerciseIds(new ArrayList<>());
        fakeExerciseVM.setExercises(new ArrayList<>());

        setField(fragment, "exerciseViewModel", fakeExerciseVM);
        setField(fragment, "assignmentViewModel", fakeAssignmentVM);

        // Rest des Tests bleibt gleich
    }

    @Test
    public void testAdapterIsUpdated() throws Exception {
        TrainingExerciseFragment fragment = TrainingExerciseFragment.newInstance(1, "Test Training");

        Application application = ApplicationProvider.getApplicationContext();
        FakeExerciseViewModel fakeExerciseVM = new FakeExerciseViewModel(application);
        FakeAssignmentViewModel fakeAssignmentVM = new FakeAssignmentViewModel(application);

        // Rest des Tests bleibt gleich
    }

    public static class FakeAssignmentViewModel extends TrainingdayExerciseAssignmentViewModel {
        private List<Integer> exerciseIds = new ArrayList<>();

        public FakeAssignmentViewModel(Application application) { // Konstruktor mit Application
            super(application);
        }

        public void setExerciseIds(List<Integer> ids) {
            this.exerciseIds = new ArrayList<>(ids);
        }

        @Override
        public void getExerciseIdsForTrainingday(int trainingdayId, Consumer<List<Integer>> callback) {
            callback.accept(exerciseIds);
        }
    }

    public static class FakeExerciseViewModel extends ExerciseViewModel {
        private List<Exercise> exercises = new ArrayList<>();

        public FakeExerciseViewModel(Application application) { // Konstruktor mit Application
            super(application);
        }

        public void setExercises(List<Exercise> exercises) {
            this.exercises = exercises;
        }

        @Override
        public void loadExercisesByIds(List<Integer> exerciseIds, Consumer<List<Exercise>> callback) {
            callback.accept(exercises);
        }
    }
}
