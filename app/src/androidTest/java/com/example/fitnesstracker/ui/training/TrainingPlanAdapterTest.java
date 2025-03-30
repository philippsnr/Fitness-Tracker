package com.example.fitnesstracker.ui.training;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.Trainingplan;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TrainingPlanAdapterTest {

    /**
     * Eine einfache Test-Activity, die einen RecyclerView enthält.
     */
    public static class TestActivity extends AppCompatActivity {
        RecyclerView recyclerView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            recyclerView = new RecyclerView(this);
            recyclerView.setId(View.generateViewId());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            setContentView(recyclerView);
        }
    }

    /**
     * Testet, ob der Adapter korrekt Daten bindet und die Klick-Listener funktionieren.
     */
    @Test
    public void testAdapterItemCountAndClickCallbacks() {
        // Starte die Test-Activity
        ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class);
        scenario.onActivity(activity -> {
            // Erstelle Testdaten
            List<Trainingplan> plans = new ArrayList<>();
            plans.add(new Trainingplan(1, "Plan A", true));
            plans.add(new Trainingplan(2, "Plan B", false));

            // Erstelle einen Test-Listener, der Klicks protokolliert
            TestListener listener = new TestListener();

            // Erstelle den Adapter
            TrainingPlanAdapter adapter = new TrainingPlanAdapter(plans, listener);
            activity.recyclerView.setAdapter(adapter);

            // Erzwinge Layout und Binding des RecyclerView
            adapter.notifyDataSetChanged();
            activity.recyclerView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            activity.recyclerView.layout(0, 0, 1000, 1000);

            // Prüfe die Item-Anzahl
            Assert.assertEquals("Item count should be 2", 2, adapter.getItemCount());

            // Hole den ViewHolder für das erste Element
            RecyclerView.ViewHolder vh = activity.recyclerView.findViewHolderForAdapterPosition(0);
            Assert.assertNotNull("ViewHolder for first item should not be null", vh);
            View itemView = vh.itemView;

            // Prüfe, ob der Name richtig gesetzt wurde
            TextView nameView = itemView.findViewById(R.id.tvPlanName);
            Assert.assertEquals("Plan A", nameView.getText().toString());

            // Simuliere Klicks auf die Icons und prüfe, ob der Listener korrekt reagiert

            // Klick auf das "View"-Icon
            ImageView ivView = itemView.findViewById(R.id.ivView);
            ivView.performClick();
            Assert.assertEquals("onViewClick should be called with position 0", 0, listener.viewClickedPosition);

            // Klick auf das "Edit"-Icon
            ImageView ivEdit = itemView.findViewById(R.id.ivEdit);
            ivEdit.performClick();
            Assert.assertEquals("onEditClick should be called with position 0", 0, listener.editClickedPosition);

            // Klick auf das "Delete"-Icon
            ImageView ivDelete = itemView.findViewById(R.id.ivDelete);
            ivDelete.performClick();
            Assert.assertEquals("onDeleteClick should be called with position 0", 0, listener.deleteClickedPosition);

            // Teste die updatePlans()-Methode: Ersetze die Liste mit einem neuen Item
            List<Trainingplan> newPlans = new ArrayList<>();
            newPlans.add(new Trainingplan(3, "Plan C", true));
            adapter.updatePlans(newPlans);
            Assert.assertEquals("After update, item count should be 1", 1, adapter.getItemCount());

            // Erzwinge ein Rebinding nach dem Update
            adapter.notifyDataSetChanged();
            activity.recyclerView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            activity.recyclerView.layout(0, 0, 1000, 1000);

            RecyclerView.ViewHolder vh2 = activity.recyclerView.findViewHolderForAdapterPosition(0);
            Assert.assertNotNull("ViewHolder for updated first item should not be null", vh2);
            TextView nameView2 = vh2.itemView.findViewById(R.id.tvPlanName);
            Assert.assertEquals("Plan C", nameView2.getText().toString());
        });
    }

    /**
     * Ein einfacher Test-Listener, der die aufgerufenen Positionen speichert.
     */
    private static class TestListener implements TrainingPlanAdapter.OnItemClickListener {
        int viewClickedPosition = -1;
        int editClickedPosition = -1;
        int deleteClickedPosition = -1;

        @Override
        public void onViewClick(int position) {
            viewClickedPosition = position;
        }

        @Override
        public void onEditClick(int position) {
            editClickedPosition = position;
        }

        @Override
        public void onDeleteClick(int position) {
            deleteClickedPosition = position;
        }
    }
}
