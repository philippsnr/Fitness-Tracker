package com.example.fitnesstracker.ui.progression;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.UserInformation;
import com.example.fitnesstracker.viewmodel.ExerciseSetViewModel;
import com.example.fitnesstracker.viewmodel.UserInformationViewModel;
import com.example.fitnesstracker.viewmodel.UserViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Fragment zur Darstellung des Fortschritts, inklusive Gewichtsentwicklung, Trainingsdaten und BMI.
 * <p>
 * Dieses Fragment zeigt Diagramme für das Gewicht (und ggf. KFA) sowie Trainingsdaten (Sets pro Woche)
 * an. Zudem können Benutzer neue Daten hinzufügen und ihr Trainingsziel bearbeiten.
 * </p>
 */
public class ProgressionFragment extends Fragment {

    private LineChart weightChart;
    private BarChart exerciseChart;
    private UserInformationViewModel userInformationViewModel;
    private ExerciseSetViewModel exerciseSetViewModel;
    private UserViewModel userViewModel;
    private TextView txtGoal;
    private TextView bmiTextView;
    private View bmiBorder;

    /**
     * Erstellt und initialisiert die View des Fragments.
     *
     * @param inflater           Der LayoutInflater zum Aufblasen der View.
     * @param container          Der Container, in dem die View platziert wird.
     * @param savedInstanceState Falls vorhanden, der vorherige Zustand des Fragments.
     * @return Die erstellte View des ProgressionFragments.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progression, container, false);

        initializeElements(view);

        Button btnAddData = view.findViewById(R.id.btnAddData);
        btnAddData.setOnClickListener(v -> showAddDataDialog());
        ImageView btnEditGoal = view.findViewById(R.id.btnEditGoal);
        btnEditGoal.setOnClickListener(v -> showEditGoalDialog());

        loadUserGoal();
        loadWeightData();
        loadBMI();
        loadExerciseData();

        return view;
    }

    /**
     * Initialisiert die UI-Elemente und ViewModel-Instanzen.
     *
     * @param view Die Wurzel-View des Fragments.
     */
    private void initializeElements(View view) {
        weightChart = view.findViewById(R.id.weightChart);
        exerciseChart = view.findViewById(R.id.exerciseChart);
        bmiTextView = view.findViewById(R.id.bmi_value);
        userInformationViewModel = new ViewModelProvider(this).get(UserInformationViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        exerciseSetViewModel = new ViewModelProvider(this).get(ExerciseSetViewModel.class);
        txtGoal = view.findViewById(R.id.txtGoal);
        bmiBorder = view.findViewById(R.id.bmi_border);
    }

    /**
     * Lädt alle gespeicherten Gewichtsdaten aus dem ViewModel und aktualisiert das Gewichtschart.
     */
    private void loadWeightData() {
        userInformationViewModel.getAllUserInformation(dataList -> {
            if (dataList != null && !dataList.isEmpty()) {
                updateWeightChart(dataList);
            }
        });
    }

    /**
     * Aktualisiert das Gewichtschart mit den übergebenen Benutzerinformationen.
     *
     * @param dataList Eine Liste von {@link UserInformation}-Objekten.
     */
    private void updateWeightChart(List<UserInformation> dataList) {
        if (!isWeightChartUpdateValid(dataList)) return;

        long baseDate = dataList.get(0).getDate().getTime();

        List<Entry> weightEntries = new ArrayList<>();
        List<Entry> kfaEntries = new ArrayList<>();
        populateWeightChartEntries(dataList, baseDate, weightEntries, kfaEntries);

        requireActivity().runOnUiThread(() -> configureWeightChart(baseDate, weightEntries, kfaEntries));
    }

    /**
     * Überprüft, ob die Gewichtschart-Aktualisierung durchgeführt werden kann.
     *
     * @param dataList Die Liste der Benutzerinformationen.
     * @return {@code true}, wenn alle Voraussetzungen erfüllt sind, andernfalls {@code false}.
     */
    private boolean isWeightChartUpdateValid(List<UserInformation> dataList) {
        return getContext() != null && weightChart != null && dataList != null && !dataList.isEmpty();
    }

    /**
     * Erstellt Chart-Einträge für Gewicht und KFA basierend auf den übergebenen Daten.
     *
     * @param dataList      Die Liste der Benutzerinformationen.
     * @param baseDate      Das Basisdatum als Referenzpunkt.
     * @param weightEntries Die Liste, in die die Gewichtseinträge hinzugefügt werden.
     * @param kfaEntries    Die Liste, in die die KFA-Einträge (falls vorhanden) hinzugefügt werden.
     */
    private void populateWeightChartEntries(List<UserInformation> dataList, long baseDate, List<Entry> weightEntries, List<Entry> kfaEntries) {
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
     * Konfiguriert das Gewichtschart mit den übergebenen Daten und aktualisiert die Darstellung.
     *
     * @param baseDate      Das Basisdatum für die X-Achse.
     * @param weightEntries Die Einträge für das Gewicht.
     * @param kfaEntries    Die Einträge für den KFA (Körperfettanteil).
     */
    private void configureWeightChart(long baseDate, List<Entry> weightEntries, List<Entry> kfaEntries) {
        LineDataSet weightDataSet = createWeightDataSet(weightEntries, "Gewicht (kg)", Color.WHITE, YAxis.AxisDependency.LEFT, true);

        LineData lineData = kfaEntries.isEmpty() ?
                new LineData(weightDataSet) :
                new LineData(weightDataSet, createWeightDataSet(kfaEntries, "KFA (%)", R.color.neon_blue, YAxis.AxisDependency.RIGHT, false));

        weightChart.setData(lineData);
        weightChart.getAxisRight().setEnabled(!kfaEntries.isEmpty());
        configureWeightChartAxes(baseDate);
        configureWeightChartLegend();
        weightChart.setExtraOffsets(0f, 0f, 0f, 15f);
        weightChart.invalidate();
    }

    /**
     * Erstellt und konfiguriert einen Datensatz für das Gewichtschart.
     *
     * @param entries Die Einträge, die im Datensatz enthalten sein sollen.
     * @param label   Die Bezeichnung des Datensatzes.
     * @param color   Die Farbe für den Datensatz.
     * @param axis    Die zugehörige Y-Achse.
     * @param filled  Legt fest, ob der Datensatz gefüllt dargestellt wird.
     * @return Den konfigurierten {@link LineDataSet}-Datensatz.
     */
    private LineDataSet createWeightDataSet(List<Entry> entries, String label, int color, YAxis.AxisDependency axis, boolean filled) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setValueTextSize(12f);
        dataSet.setCircleRadius(4f);
        dataSet.setLineWidth(2f);
        dataSet.setDrawValues(false);
        dataSet.setDrawFilled(filled);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setAxisDependency(axis);
        if (filled) {
            dataSet.setFillColor(color);
            dataSet.setFillAlpha(80);
        }
        return dataSet;
    }

    /**
     * Konfiguriert die Achsen des Gewichtscharts, einschließlich X-Achsen-Formatierung und Farbgebung.
     *
     * @param baseDate Das Basisdatum als Referenz für die X-Achse.
     */
    private void configureWeightChartAxes(long baseDate) {
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

        // Setzt die Farben für die Achsenbeschriftung
        weightChart.getXAxis().setTextColor(Color.WHITE);
        weightChart.getAxisLeft().setTextColor(Color.WHITE);
        weightChart.getAxisRight().setTextColor(Color.WHITE);
    }

    /**
     * Konfiguriert die Legende des Gewichtscharts.
     */
    private void configureWeightChartLegend() {
        Legend legend = weightChart.getLegend();
        legend.setTextColor(Color.WHITE);
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
     * Lädt die gespeicherten Trainingsdaten (ExerciseSets) und aktualisiert das Balkendiagramm.
     */
    private void loadExerciseData() {
        exerciseSetViewModel.loadSetsPerWeek(setsPerWeek -> {
            if (setsPerWeek != null && !setsPerWeek.isEmpty()) {
                updateExerciseChart(setsPerWeek);
            }
        });
    }

    /**
     * Aktualisiert das Balkendiagramm mit den Trainingsdaten.
     *
     * @param setsPerWeek Eine Map, in der die Schlüssel die Kalenderwoche und die Werte die Anzahl der Sets darstellen.
     */
    private void updateExerciseChart(Map<Integer, Integer> setsPerWeek) {
        List<BarEntry> barEntries = new ArrayList<>();
        List<Integer> weeks = new ArrayList<>(setsPerWeek.keySet());
        Collections.sort(weeks);
        for (Integer week : weeks) {
            barEntries.add(new BarEntry(week, setsPerWeek.get(week)));
        }
        requireActivity().runOnUiThread(() -> configureExerciseChart(barEntries));
    }

    /**
     * Konfiguriert das Balkendiagramm mit den übergebenen Einträgen.
     *
     * @param barEntries Die Balkeneinträge, die angezeigt werden sollen.
     */
    private void configureExerciseChart(List<BarEntry> barEntries) {
        BarDataSet dataSet = createBarDataSet(barEntries);
        BarData barData = new BarData(dataSet);
        exerciseChart.setData(barData);
        configureExerciseChartAxes();
        configureExerciseChartLegend();
        exerciseChart.invalidate();
    }

    /**
     * Erzeugt das {@link BarDataSet} für das Balkendiagramm und wendet Formatierungen an.
     *
     * @param barEntries Die Einträge für das Balkendiagramm.
     * @return Das konfigurierte {@link BarDataSet}.
     */
    private BarDataSet createBarDataSet(List<BarEntry> barEntries) {
        BarDataSet dataSet = new BarDataSet(barEntries, "Sets pro Woche");
        dataSet.setColors(getBarColors(barEntries));
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueFormatter(createValueFormatter());
        return dataSet;
    }

    /**
     * Erstellt einen {@link ValueFormatter}, der die Balkenwerte als ganze Zahlen formatiert.
     *
     * @return Den erstellten {@link ValueFormatter}.
     */
    private ValueFormatter createValueFormatter() {
        return new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "%d", (int) value);
            }
        };
    }

    /**
     * Ermittelt die Farben für die Balken basierend auf der Anzahl der Sets.
     *
     * @param barEntries Die Liste der Balkeneinträge.
     * @return Eine Liste von Farben, die den jeweiligen Balken zugeordnet werden.
     */
    private List<Integer> getBarColors(List<BarEntry> barEntries) {
        List<Integer> colors = new ArrayList<>();
        for (BarEntry entry : barEntries) {
            colors.add(getSetColor((int) entry.getY()));
        }
        return colors;
    }

    /**
     * Bestimmt die Farbe für einen Balken basierend auf der Anzahl der Sets.
     *
     * @param count Die Anzahl der Sets.
     * @return Die Farbe als int-Wert.
     */
    private int getSetColor(int count) {
        int colorResource;
        if (count < 8) {
            colorResource = R.color.low_exercise_sets;
        } else if (count < 12) {
            colorResource = R.color.medium_exercise_sets;
        } else if (count < 16) {
            colorResource = R.color.good_exercise_sets;
        } else {
            colorResource = R.color.optimal_exercise_sets;
        }
        return ContextCompat.getColor(requireContext(), colorResource);
    }

    /**
     * Konfiguriert die Achsen des Balkendiagramms.
     */
    private void configureExerciseChartAxes() {
        XAxis xAxis = exerciseChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "KW" + ((int) value);
            }
        });
        exerciseChart.getAxisLeft().setEnabled(false);
        exerciseChart.getAxisRight().setEnabled(false);
    }

    /**
     * Konfiguriert die Legende des Balkendiagramms und deaktiviert diese.
     */
    private void configureExerciseChartLegend() {
        exerciseChart.getLegend().setEnabled(false);
        Description description = new Description();
        description.setText("");
        exerciseChart.setDescription(description);
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
     * Verarbeitet die Eingabe im Dialog zur Dateneingabe und speichert die neuen Daten.
     *
     * @param etWeight Das Eingabefeld für das Gewicht.
     * @param etHeight Das Eingabefeld für die Größe.
     * @param etKfa    Das Eingabefeld für den KFA.
     */
    private void handleAddDataSubmit(EditText etWeight, EditText etHeight, EditText etKfa) {
        UserInformation newData = convertInputToUserInformation(etWeight, etHeight, etKfa);
        if (newData != null) {
            userInformationViewModel.writeUserInformation(newData);
            loadWeightData(); // Chart aktualisieren
            loadBMI();      // BMI aktualisieren
        }
    }

    /**
     * Konvertiert die Eingabewerte der EditText-Felder in ein {@link UserInformation}-Objekt.
     *
     * @param etWeight Das Eingabefeld für das Gewicht.
     * @param etHeight Das Eingabefeld für die Größe.
     * @param etKfa    Das Eingabefeld für den KFA.
     * @return Ein {@link UserInformation}-Objekt mit den eingegebenen Werten oder {@code null}, falls ein Fehler auftritt.
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

    /**
     * Lädt das aktuelle Trainingsziel aus dem {@link UserViewModel} und zeigt es in der TextView an.
     */
    private void loadUserGoal() {
        userViewModel.getUserGoal(goal -> {
            if (goal != null) {
                txtGoal.setText("Ziel: " + goal);
            } else {
                txtGoal.setText("Ziel: Nicht gesetzt");
            }
        });
    }

    /**
     * Zeigt einen Dialog zur Auswahl des Trainingsziels an.
     */
    private void showEditGoalDialog() {
        View dialogView = getDialogView();
        RadioGroup radioGroupGoals = dialogView.findViewById(R.id.radioGroupGoals);
        preselectCurrentGoal(radioGroupGoals);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Trainingsziel wählen");
        builder.setView(dialogView);
        builder.setPositiveButton("Speichern", (dialog, which) -> saveSelectedGoal(radioGroupGoals));
        builder.setNegativeButton("Abbrechen", null);
        builder.create().show();
    }

    /**
     * Erstellt und gibt die View für den Dialog zur Zielauswahl zurück.
     *
     * @return Die Dialog-View.
     */
    private View getDialogView() {
        return getLayoutInflater().inflate(R.layout.progression_dialog_edit_goal, null);
    }

    /**
     * Präselektiert im Zielauswahl-Dialog das aktuell gesetzte Trainingsziel.
     *
     * @param radioGroupGoals Die RadioGroup, in der die Ziele ausgewählt werden.
     */
    private void preselectCurrentGoal(RadioGroup radioGroupGoals) {
        String currentGoal = txtGoal.getText().toString();

        if (currentGoal.contains("Abnehmen")) {
            radioGroupGoals.check(R.id.rbLoseWeight);
        } else if (currentGoal.contains("Gewicht halten")) {
            radioGroupGoals.check(R.id.rbMaintainWeight);
        } else if (currentGoal.contains("Zunehmen")) {
            radioGroupGoals.check(R.id.rbGainWeight);
        }
    }

    /**
     * Speichert das ausgewählte Trainingsziel und aktualisiert die UI.
     *
     * @param radioGroupGoals Die RadioGroup, aus der das Ziel ausgewählt wurde.
     */
    private void saveSelectedGoal(RadioGroup radioGroupGoals) {
        int selectedId = radioGroupGoals.getCheckedRadioButtonId();
        String newGoal = getGoalFromSelection(selectedId);

        txtGoal.setText("Ziel: " + newGoal);
        userViewModel.updateUserGoal(newGoal);
    }

    /**
     * Bestimmt das Trainingsziel basierend auf der ausgewählten RadioButton-ID.
     *
     * @param selectedId Die ID des ausgewählten RadioButtons.
     * @return Das entsprechende Trainingsziel als String.
     */
    private String getGoalFromSelection(int selectedId) {
        if (selectedId == R.id.rbLoseWeight) {
            return "Abnehmen";
        } else if (selectedId == R.id.rbMaintainWeight) {
            return "Gewicht halten";
        } else if (selectedId == R.id.rbGainWeight) {
            return "Zunehmen";
        }
        return "";
    }

    /**
     * Lädt den BMI aus dem {@link UserInformationViewModel} und aktualisiert die BMI-UI.
     */
    private void loadBMI() {
        userInformationViewModel.getBMI(bmi -> requireActivity().runOnUiThread(() -> updateBmiUI(bmi)));
    }

    /**
     * Aktualisiert die UI-Komponenten für die BMI-Anzeige.
     *
     * @param bmi Der berechnete BMI-Wert.
     */
    private void updateBmiUI(double bmi) {
        if (getContext() == null || bmiBorder == null) return;

        bmiTextView.setText(formatBmiValue(bmi));
        bmiBorder.getBackground().setTint(getBmiColor(bmi));
    }

    /**
     * Formatiert den BMI-Wert als String mit einer Nachkommastelle.
     *
     * @param bmi Der BMI-Wert.
     * @return Der formatierte BMI-Wert als String.
     */
    private String formatBmiValue(double bmi) {
        return String.format(Locale.getDefault(), "%.1f", bmi);
    }

    /**
     * Bestimmt anhand des BMI-Werts die entsprechende Farbe.
     *
     * @param bmi Der BMI-Wert.
     * @return Die entsprechende Farbe als int-Wert.
     */
    private int getBmiColor(double bmi) {
        int colorResource;

        if (bmi < 18.5) {
            colorResource = R.color.bmi_underweight;
        } else if (bmi < 25) {
            colorResource = R.color.bmi_normal;
        } else if (bmi < 30) {
            colorResource = R.color.bmi_overweight;
        } else {
            colorResource = R.color.bmi_obese;
        }

        return ContextCompat.getColor(requireContext(), colorResource);
    }
}
