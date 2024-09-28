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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracky.DashboardViewModel;
import com.example.expensetracky.databinding.FragmentDashboardBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {
    private FirebaseUser currentUser;
    private FragmentDashboardBinding binding;
    private PieChart pieChart;
    private FirebaseFirestore db;
    private CategoryAdapter categoryAdapter;
    private RecyclerView categoryRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize PieChart
        pieChart = binding.pieChart;
        categoryRecyclerView = binding.categoryRecyclerView;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Fetch data and set up the PieChart
        loadTransactionData();

        return root;
    }

    // Fetch transactions from Firestore, summing the amounts by category
    private void loadTransactionData() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null) {
            String userId = currentUser.getUid();

            db.collection("users").document(userId).collection("transactions")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();

                            // Map to store the total amounts for each category
                            Map<String, Float> categoryTotals = new HashMap<>();

                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                String category = document.getString("category");
                                Double amount = document.getDouble("amount");

                                // Ensure non-null category and amount
                                if (category != null && amount != null) {
                                    // Add the amount to the respective category
                                    if (categoryTotals.containsKey(category)) {
                                        categoryTotals.put(category, categoryTotals.get(category) + amount.floatValue());
                                    } else {
                                        categoryTotals.put(category, amount.floatValue());
                                    }
                                }
                            }

                            // Now that we have the totals for each category, we can update the pie chart
                            updatePieChart(categoryTotals);
                            updateCategoryList(categoryTotals);
                        } else {
                            Toast.makeText(getActivity(), "Failed to load transactions", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Update the PieChart with the fetched data
    // Update the PieChart with the fetched data
    // Update the PieChart with the fetched data
    private void updatePieChart(Map<String, Float> categoryTotals) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // Convert the categoryTotals map to PieEntry objects
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        // Create PieDataSet and configure the PieChart
        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(new int[]{Color.rgb(255, 127, 0), Color.rgb(254, 57, 57), Color.rgb(236, 164, 164)}); // Sample colors
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(16f);
        dataSet.setSliceSpace(4f);

        // Position the labels outside the slices
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        // Restore the lines (arrows) to connect labels to the slices
        dataSet.setValueLinePart1OffsetPercentage(60.f);  // Extend the offset of the first part of the line
        dataSet.setValueLinePart1Length(1f);  // First segment length
        dataSet.setValueLinePart2Length(1f);  // Second segment length
        dataSet.setValueLineWidth(2f);  // Thickness of the line
        dataSet.setValueLineColor(Color.RED);  // Line color

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);

        // Set the hole size to make the pie chart circle slightly bigger
        pieChart.setHoleRadius(15f);  // Increase the hole radius for a bigger chart circle
        pieChart.setTransparentCircleRadius(22f);  // Adjust the transparent circle slightly larger than the hole

        // Optional: Add padding (extra offsets) around the chart
        pieChart.setExtraOffsets(25, 25, 25, 25);  // Padding around the pie chart

        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.getDescription().setEnabled(false);  // Disable chart description
        pieChart.getDescription().setText("");
        pieChart.setTransparentCircleColor(Color.LTGRAY);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setDrawEntryLabels(true);  // Draw labels outside slices
        pieChart.setEntryLabelColor(Color.parseColor("#B21807"));
        pieChart.setEntryLabelTextSize(14f);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.animateY(1400);  // Animation duration
        pieChart.invalidate();    // Refresh the chart

        // Optionally: Disable the legend if not needed
        pieChart.getLegend().setEnabled(false);

        // Set up click listener for pie slices
        setupClickListener();
    }



    private void updateCategoryList(Map<String, Float> categoryTotals) {
        List<Map.Entry<String, Float>> categoryList = new ArrayList<>(categoryTotals.entrySet());
        categoryAdapter = new CategoryAdapter(categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }
    private void setupClickListener() {
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // Handle the click on a pie slice
                if (e instanceof PieEntry) {
                    PieEntry pieEntry = (PieEntry) e;
                    String message = "Selected: " + pieEntry.getLabel() + " - " + pieEntry.getValue();
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
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
