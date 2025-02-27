package com.example.fitnesstracker.ui.progress;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.UserInformation;
import com.example.fitnesstracker.viewmodel.UserInformationViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgressionFragment extends Fragment {

    private LineChart weightChart;
    private UserInformationViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progression, container, false);
        weightChart = view.findViewById(R.id.weightChart);

        // Button initialisieren (ersetze btnAddData durch fabAddData, falls du den FloatingActionButton verwendest)
        Button btnAddData = view.findViewById(R.id.btnAddData);
        btnAddData.setOnClickListener(v -> showAddDataDialog());

        // ViewModel initialisieren
        viewModel = new ViewModelProvider(this).get(UserInformationViewModel.class);

        // Daten abrufen und den Graphen aktualisieren
        loadWeightData();

        return view;
    }

    private void loadWeightData() {
        viewModel.getAllUserInformation(dataList -> {
            if (dataList != null && !dataList.isEmpty()) {
                updateChart(dataList);
            }
        });
    }

    private void updateChart(List<UserInformation> dataList) {
        if (getContext() == null || weightChart == null || dataList == null || dataList.isEmpty()) {
            return;
        }

        // Daten nach Datum sortieren (aufsteigend)
        Collections.sort(dataList, Comparator.comparing(UserInformation::getDate));

        // Basis-Datum festlegen (erstes Datum in der sortierten Liste)
        final long baseDate = dataList.get(0).getDate().getTime();
        final float millisInDay = 86400000f; // Millisekunden pro Tag

        List<Entry> entries = new ArrayList<>();

        // Für jedes Datum den Unterschied in Tagen als x-Wert berechnen
        for (UserInformation info : dataList) {
            float diffDays = (info.getDate().getTime() - baseDate) / millisInDay;
            // Da weight jetzt ein double ist, casten wir in float
            entries.add(new Entry(diffDays, (float) info.getWeight()));
        }

        getActivity().runOnUiThread(() -> {
            LineDataSet dataSet = new LineDataSet(entries, "Gewichtsverlauf");
            dataSet.setValueTextSize(12f);
            dataSet.setCircleRadius(4f);
            dataSet.setLineWidth(2f);
            dataSet.setDrawValues(false);
            dataSet.setDrawFilled(true);
            dataSet.setFillColor(ContextCompat.getColor(requireContext(), R.color.primary));
            dataSet.setFillAlpha(80);
            dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.primary));
            dataSet.setCircleColor(ContextCompat.getColor(requireContext(), R.color.primary));

            LineData lineData = new LineData(dataSet);
            weightChart.setData(lineData);

            weightChart.getAxisRight().setEnabled(false);
            weightChart.getAxisLeft().setDrawGridLines(false);
            weightChart.getXAxis().setDrawGridLines(false);
            weightChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            weightChart.setExtraBottomOffset(10f);

            // X-Achse formatieren: Den x-Wert zurück in ein Datum konvertieren
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());
            weightChart.getXAxis().setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    long millis = baseDate + (long) (value * millisInDay);
                    return dateFormat.format(new Date(millis));
                }
            });
            // Optional: Granularität setzen, damit z. B. immer mindestens ein Tag angezeigt wird
            weightChart.getXAxis().setGranularity(1f);

            weightChart.getLegend().setEnabled(false);
            Description description = new Description();
            description.setText("");
            weightChart.setDescription(description);

            int chartBackgroundColor = ContextCompat.getColor(getContext(), R.color.background);
            weightChart.setBackgroundColor(chartBackgroundColor);

            XAxis xAxis = weightChart.getXAxis();
            xAxis.setTextColor(android.graphics.Color.WHITE);
            xAxis.setAxisLineColor(android.graphics.Color.WHITE);
            xAxis.setDrawGridLines(false);

            YAxis leftYAxis = weightChart.getAxisLeft();
            leftYAxis.setTextColor(android.graphics.Color.WHITE);
            leftYAxis.setAxisLineColor(android.graphics.Color.WHITE);
            leftYAxis.setDrawGridLines(false);

            YAxis rightYAxis = weightChart.getAxisRight();
            rightYAxis.setTextColor(android.graphics.Color.WHITE);
            rightYAxis.setAxisLineColor(android.graphics.Color.WHITE);
            rightYAxis.setDrawGridLines(false);

            weightChart.invalidate();
        });
    }

    private void showAddDataDialog() {
        // Dialog-Layout laden (ohne Datumseingabe)
        View dialogView = getLayoutInflater().inflate(R.layout.progression_dialog_add_data, null);
        EditText etWeight = dialogView.findViewById(R.id.etWeight);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Neue Daten eingeben");
        builder.setView(dialogView);
        builder.setPositiveButton("Speichern", (dialog, which) -> {
            String weightStr = etWeight.getText().toString().trim();

            if (!weightStr.isEmpty()) {
                try {
                    double weight = Double.parseDouble(weightStr);
                    // Aktuelles Datum verwenden
                    Date currentDate = new Date();

                    // Beispielwerte für weitere Felder (z.B. Höhe, KFA)
                    int height = 170; // Beispielwert
                    int kfa = 20;     // Beispielwert

                    // Neue UserInformation erstellen (Verwendung des Date-Konstruktors)
                    UserInformation newData = new UserInformation(0, 1, currentDate, height, weight, kfa);
                    viewModel.writeUserInformation(newData);

                    // Nach dem Speichern den Chart aktualisieren
                    loadWeightData();

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    // Optional: Fehlermeldung anzeigen
                }
            }
        });
        builder.setNegativeButton("Abbrechen", null);
        builder.create().show();
    }
}
