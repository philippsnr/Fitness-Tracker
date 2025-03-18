package com.example.fitnesstracker.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.fitnesstracker.model.Trainingplan;
import com.example.fitnesstracker.repository.TrainingplanRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {28}) // Android API Level
public class TrainingplanViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private TrainingplanRepository mockRepository;

    private TrainingplanViewModel viewModel;
    private ExecutorService testExecutor;
    private Application application;

    @Before
    public void setUp() {
        application = RuntimeEnvironment.getApplication();
        testExecutor = Executors.newSingleThreadExecutor();

        // Create ViewModel with real dependencies but mock repository
        viewModel = new TrainingplanViewModel(application);
    }
    private void setFieldViaReflection(Object target, String fieldName, Object value)
            throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Überschreibt Zugriffsbeschränkung
        field.set(target, value);
    }

    @After
    public void tearDown() throws InterruptedException {
        testExecutor.shutdown();
        if (!testExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
            fail("Executor did not terminate in time");
        }
    }

    @Test
    public void testLoadAllTrainingplansSuccess() throws Exception {
        // Arrange
        List<Trainingplan> mockPlans = Arrays.asList(
                new Trainingplan(1, "Plan 1", true),
                new Trainingplan(2, "Plan 2", false)
        );
        when(mockRepository.getAllTrainingplans()).thenReturn(mockPlans);

        TestConsumer<List<Trainingplan>> testCallback = new TestConsumer<>();
        TestConsumer<Exception> testError = new TestConsumer<>();

        // Act
        viewModel.loadAllTrainingplans(testCallback, testError);
        testExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);

        // Assert
        assertEquals(mockPlans, testCallback.value);
        assertNull(testError.value);
        verify(mockRepository).getAllTrainingplans();
    }

    @Test
    public void testSetActiveTrainingplanSuccess() throws Exception {
        // Arrange
        TestRunnable testRunnable = new TestRunnable();
        TestConsumer<Exception> testError = new TestConsumer<>();

        // Act
        viewModel.setActiveTrainingplan(2, testRunnable, testError);
        testExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);

        // Assert
        verify(mockRepository).setNewActiveTrainingPlan(2);
        assertTrue(testRunnable.wasExecuted);
        assertNull(testError.value);
    }

    @Test
    public void testLoadActiveTrainingplanSuccess() throws Exception {
        // Arrange
        Trainingplan expectedPlan = new Trainingplan(1, "Active Plan", true);
        when(mockRepository.getActiveTrainingplan()).thenReturn(expectedPlan);

        TestConsumer<Trainingplan> testCallback = new TestConsumer<>();
        TestConsumer<Exception> testError = new TestConsumer<>();

        // Act
        viewModel.loadActiveTrainingplan(testCallback, testError);
        testExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);

        // Assert
        assertEquals(expectedPlan, testCallback.value);
        assertNull(testError.value);
    }

    @Test
    public void testAddTrainingplanSuccess() throws Exception {
        // Arrange
        Trainingplan newPlan = new Trainingplan("New Plan", false);
        TestRunnable testRunnable = new TestRunnable();
        TestConsumer<Exception> testError = new TestConsumer<>();

        // Act
        viewModel.addTrainingplan(newPlan, testRunnable, testError);
        testExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);

        // Assert
        verify(mockRepository).addTrainingplan(newPlan);
        assertTrue(testRunnable.wasExecuted);
        assertNull(testError.value);
    }

    @Test
    public void testUpdateTrainingplanSuccess() throws Exception {
        // Arrange
        Trainingplan updatedPlan = new Trainingplan(1, "Updated Plan", true);
        TestRunnable testRunnable = new TestRunnable();
        TestConsumer<Exception> testError = new TestConsumer<>();

        // Act
        viewModel.updateTrainingplan(updatedPlan, testRunnable, testError);
        testExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);

        // Assert
        verify(mockRepository).updateTrainingplanName(updatedPlan);
        assertTrue(testRunnable.wasExecuted);
        assertNull(testError.value);
    }

    @Test
    public void testDeleteTrainingplanSuccess() throws Exception {
        // Arrange
        Trainingplan planToDelete = new Trainingplan(1, "Delete Plan", false);
        TestRunnable testRunnable = new TestRunnable();
        TestConsumer<Exception> testError = new TestConsumer<>();

        // Act
        viewModel.deleteTrainingplan(planToDelete, testRunnable, testError);
        testExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);

        // Assert
        verify(mockRepository).deleteTrainingplan(planToDelete);
        assertTrue(testRunnable.wasExecuted);
        assertNull(testError.value);
    }

    // Helper classes
    private static class TestConsumer<T> implements Consumer<T> {
        T value;

        @Override
        public void accept(T t) {
            this.value = t;
        }
    }

    private static class TestRunnable implements Runnable {
        boolean wasExecuted = false;

        @Override
        public void run() {
            wasExecuted = true;
        }
    }
}