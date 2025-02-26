package com.example.fitnesstracker.ui.progress;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
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
        List<Entry> entries = new ArrayList<>();

        // Sortiere die Liste nach Datum (älteste zuerst)
        Collections.sort(dataList, Comparator.comparing(UserInformation::getDate));

        // Daten in Entries umwandeln (X = Index, Y = Gewicht)
        for (int i = 0; i < dataList.size(); i++) {
            entries.add(new Entry(i, dataList.get(i).getWeight()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Gewichtsverlauf");
        dataSet.setValueTextSize(12f);
        dataSet.setCircleRadius(4f);
        dataSet.setLineWidth(2f);
        dataSet.setDrawValues(false);

        // Farben setzen
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.primary));
        dataSet.setCircleColor(ContextCompat.getColor(requireContext(), R.color.primary));

        LineData lineData = new LineData(dataSet);
        weightChart.setData(lineData);

        // Achsen-Anpassungen
        weightChart.getAxisRight().setEnabled(false);
        weightChart.getAxisLeft().setDrawGridLines(false);
        weightChart.getXAxis().setDrawGridLines(false);
        weightChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        weightChart.setExtraBottomOffset(10f);

        // X-Achse mit Datumswerten formatieren
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());
        weightChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < dataList.size()) {
                    return dateFormat.format(dataList.get(index).getDate());
                }
                return "";
            }
        });

        // Chart Styling
        weightChart.getLegend().setEnabled(false);
        Description description = new Description();
        description.setText("");
        weightChart.setDescription(description);
        weightChart.setBackgroundColor(Color.parseColor("#E0E0E0"));

        // Aktualisieren des Diagramms
        weightChart.invalidate();
    }
}
