package com.example.fitnesstracker.ui.progress;

import android.app.AlertDialog;
import android.graphics.Color;
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
import com.github.mikephil.charting.components.Legend;
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

    /**
     * Erstellt und initialisiert das Fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progression, container, false);
        weightChart = view.findViewById(R.id.weightChart);

        Button btnAddData = view.findViewById(R.id.btnAddData);
        btnAddData.setOnClickListener(v -> showAddDataDialog());

        viewModel = new ViewModelProvider(this).get(UserInformationViewModel.class);
        loadWeightData();

        return view;
    }

    /**
     * Lädt die gespeicherten Gewichtsdaten und aktualisiert das Diagramm.
     */
    private void loadWeightData() {
        viewModel.getAllUserInformation(dataList -> {
            if (dataList != null && !dataList.isEmpty()) {
                updateChart(dataList);
            }
        });
    }

    /**
     * Aktualisiert das Diagramm mit neuen Daten.
     */
    private void updateChart(List<UserInformation> dataList) {
        if (!isChartUpdateValid(dataList)) return;

        Collections.sort(dataList, Comparator.comparing(UserInformation::getDate));
        long baseDate = dataList.get(0).getDate().getTime();

        List<Entry> weightEntries = new ArrayList<>();
        List<Entry> kfaEntries = new ArrayList<>();
        populateChartEntries(dataList, baseDate, weightEntries, kfaEntries);

        getActivity().runOnUiThread(() -> configureChart(baseDate, weightEntries, kfaEntries));
    }

    /**
     * Überprüft, ob die Diagrammaktualisierung gültig ist.
     */
    private boolean isChartUpdateValid(List<UserInformation> dataList) {
        return getContext() != null && weightChart != null && dataList != null && !dataList.isEmpty();
    }

    /**
     * Erstellt die Einträge für das Diagramm.
     */
    private void populateChartEntries(List<UserInformation> dataList, long baseDate, List<Entry> weightEntries, List<Entry> kfaEntries) {
        final float millisInDay = 86400000f;
        for (UserInformation info : dataList) {
            float diffDays = (info.getDate().getTime() - baseDate) / millisInDay;
            weightEntries.add(new Entry(diffDays, (float) info.getWeight()));
            if (info.getKfa() != 0) {
                kfaEntries.add(new Entry(diffDays, (float) info.getKfa()));
            }
        }
    }

    /**
     * Konfiguriert das Diagramm mit den übergebenen Daten.
     */
    private void configureChart(long baseDate, List<Entry> weightEntries, List<Entry> kfaEntries) {
        LineDataSet weightDataSet = createDataSet(weightEntries, "Gewicht (kg)", R.color.primary, YAxis.AxisDependency.LEFT, true);

        LineData lineData = kfaEntries.isEmpty() ?
                new LineData(weightDataSet) :
                new LineData(weightDataSet, createDataSet(kfaEntries, "KFA (%)", R.color.blue, YAxis.AxisDependency.RIGHT, false));

        weightChart.setData(lineData);
        weightChart.getAxisRight().setEnabled(!kfaEntries.isEmpty());
        configureAxes(baseDate);
        configureLegend();
        weightChart.invalidate();
    }

    /**
     * Erstellt und konfiguriert einen Datensatz für das Diagramm.
     */
    private LineDataSet createDataSet(List<Entry> entries, String label, int colorRes, YAxis.AxisDependency axis, boolean filled) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setValueTextSize(12f);
        dataSet.setCircleRadius(4f);
        dataSet.setLineWidth(2f);
        dataSet.setDrawValues(false);
        dataSet.setDrawFilled(filled);
        dataSet.setColor(ContextCompat.getColor(requireContext(), colorRes));
        dataSet.setCircleColor(ContextCompat.getColor(requireContext(), colorRes));
        dataSet.setAxisDependency(axis);
        if (filled) {
            dataSet.setFillColor(ContextCompat.getColor(requireContext(), colorRes));
            dataSet.setFillAlpha(80);
        }
        return dataSet;
    }

    /**
     * Konfiguriert die Achsen des Diagramms.
     */
    private void configureAxes(long baseDate) {
        weightChart.getAxisLeft().setDrawGridLines(false);
        weightChart.getXAxis().setDrawGridLines(false);
        weightChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        weightChart.setExtraBottomOffset(10f);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());
        weightChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return dateFormat.format(new Date(baseDate + (long) (value * 86400000f)));
            }
        });
        weightChart.getXAxis().setGranularity(1f);
    }

    /**
     * Konfiguriert die Legende des Diagramms.
     */
    private void configureLegend() {
        Legend legend = weightChart.getLegend();
        legend.setTextColor(ContextCompat.getColor(requireContext(), R.color.text));
        legend.setForm(Legend.LegendForm.LINE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        Description description = new Description();
        description.setText("");
        weightChart.setDescription(description);
    }

    /**
     * Zeigt einen Dialog zur Eingabe neuer Nutzerdaten (Gewicht, Größe, KFA) an.
     */
    private void showAddDataDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.progression_dialog_add_data, null);
        EditText etWeight = dialogView.findViewById(R.id.etWeight);
        EditText etHeight = dialogView.findViewById(R.id.etHeight);
        EditText etKfa = dialogView.findViewById(R.id.etKfa);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Neue Daten eingeben");
        builder.setView(dialogView);
        builder.setPositiveButton("Speichern", (dialog, which) -> handleAddDataSubmit(etWeight, etHeight, etKfa));
        builder.setNegativeButton("Abbrechen", null);
        builder.create().show();
    }

    /**
     * Verarbeitet die eingegebenen Nutzerdaten und speichert sie.
     */
    private void handleAddDataSubmit(EditText etWeight, EditText etHeight, EditText etKfa) {
        UserInformation newData = convertInputToUserInformation(etWeight, etHeight, etKfa);
        if (newData != null) {
            viewModel.writeUserInformation(newData);
            loadWeightData(); // Chart aktualisieren
        }
    }

    /**
     * Konvertiert die eingegebenen Werte aus den EditText-Feldern in ein UserInformation-Objekt.
     */
    private UserInformation convertInputToUserInformation(EditText etWeight, EditText etHeight, EditText etKfa) {
        try {
            String weightStr = etWeight.getText().toString().trim();
            if (weightStr.isEmpty()) {
                return null;
            }

            double weight = Double.parseDouble(weightStr);
            String heightStr = etHeight.getText().toString().trim();
            String kfaStr = etKfa.getText().toString().trim();

            int height = !heightStr.isEmpty() ? Integer.parseInt(heightStr) : 0;
            int kfa = !kfaStr.isEmpty() ? Integer.parseInt(kfaStr) : 0;

            return new UserInformation(0, 1, new Date(), height, weight, kfa);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
