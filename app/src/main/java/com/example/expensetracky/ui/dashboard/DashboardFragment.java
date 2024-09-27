package com.example.expensetracky.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetracky.databinding.FragmentDashboardBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private PieChart pieChart;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize PieChart
        pieChart = binding.pieChart; // Assuming pieChart is defined in your FragmentDashboardBinding

        // Set up the PieChart
        setupPieChart();

        return root;
    }

    private void setupPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(30f, "Category 1"));
        entries.add(new PieEntry(50f, "Category 2"));
        entries.add(new PieEntry(20f, "Category 3"));

        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(new int[]{Color.rgb(255,127,0), Color.rgb(254,57,57), Color.rgb(236,164,164)}); // Remove getContext()
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(16f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleColor(Color.LTGRAY);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(14f);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.animateY(1400); // Animation duration
        pieChart.invalidate(); // Refresh the chart

        // Set up click listener
        setupClickListener();
    }


    private void setupClickListener() {
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            public void onValueSelected(PieEntry e, com.github.mikephil.charting.highlight.Highlight h) {
                // Handle the click on a pie slice
                String message = "Selected: " + e.getLabel() + " - " + e.getValue();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }

            /**
             * @param e The selected Entry
             * @param h The corresponding highlight object that contains information
             *          about the highlighted position such as dataSetIndex, ...
             */
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {
                // Handle case when nothing is selected
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
