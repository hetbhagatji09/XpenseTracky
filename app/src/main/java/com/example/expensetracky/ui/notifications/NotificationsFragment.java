package com.example.expensetracky.ui.notifications;

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

import com.example.expensetracky.databinding.FragmentNotificationsBinding;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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

public class NotificationsFragment extends Fragment {
    private FirebaseUser currentUser;
    private FragmentNotificationsBinding binding;
    private BarChart barChart;
    private FirebaseFirestore db;
    private RecyclerView accountRecyclerView;
    private AccountAdapter accountAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel dashboardViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize PieChart
        barChart = binding.barChart;  // Reference the PieChart from the binding
        accountRecyclerView = binding.accountRecyclerView; // Reference the RecyclerView from the binding

        // Set up RecyclerView
        accountRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
                                String account = document.getString("account");
                                Double amount = document.getDouble("amount");

                                // Ensure non-null category and amount
                                if (account != null && amount != null) {
                                    // Add the amount to the respective category
                                    if (categoryTotals.containsKey(account)) {
                                        categoryTotals.put(account, categoryTotals.get(account) + amount.floatValue());
                                    } else {
                                        categoryTotals.put(account, amount.floatValue());
                                    }
                                }
                            }

                            // Now that we have the totals for each category, we can update the pie chart
                            updateBarChart(categoryTotals);
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
    private void updateBarChart(Map<String, Float> categoryTotals) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>(); // To store the category names

        int index = 0;
        // Convert the categoryTotals map to BarEntry objects and store labels
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue()));  // BarEntry uses index and value
            labels.add(entry.getKey());  // Store category name for X-axis label
            index++;
        }

        // Create BarDataSet and configure the BarChart
        BarDataSet dataSet = new BarDataSet(entries, "Categories");
        dataSet.setColors(new int[]{Color.rgb(255, 127, 0), Color.rgb(254, 57, 57), Color.rgb(236, 164, 164)});  // Sample colors
        dataSet.setValueTextColor(Color.WHITE); // Value labels text color
        dataSet.setValueTextSize(16f); // Value labels text size

        // Create BarData object and set it to the chart
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.setFitBars(true);  // Make the bars fit the chart width
        barChart.setDrawGridBackground(false); // Disable grid background

        // X-axis configuration to show category names
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); // Set category names as X-axis labels
        xAxis.setGranularity(1f); // Ensure labels are evenly spaced
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Position labels at the bottom
        xAxis.setDrawGridLines(false); // Remove grid lines for a cleaner look
        xAxis.setLabelRotationAngle(0f); // Keep labels straight
        xAxis.setTextColor(Color.WHITE); // X-axis labels text color
        xAxis.setTextSize(12f); // X-axis labels text size

        // Y-axis configuration (optional)
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE); // Y-axis labels text color
        leftAxis.setDrawGridLines(true); // Show grid lines on the Y-axis
        barChart.getAxisRight().setEnabled(false); // Disable right Y-axis

        // Disable chart description
        barChart.getDescription().setEnabled(false);

        // Optional: Disable legend if not needed
        barChart.getLegend().setEnabled(false);

        // Add animation for the bars to rise
        barChart.animateY(2000, Easing.EaseInOutQuad); // Animate the Y values with a duration of 1400ms

        // Refresh the chart
        barChart.invalidate();
    }





    private void updateCategoryList(Map<String, Float> categoryTotals) {
        List<Map.Entry<String, Float>> categoryList = new ArrayList<>(categoryTotals.entrySet());
        accountAdapter = new AccountAdapter(categoryList);
        accountRecyclerView.setAdapter(accountAdapter);
    }
    private void setupClickListener() {
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
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
