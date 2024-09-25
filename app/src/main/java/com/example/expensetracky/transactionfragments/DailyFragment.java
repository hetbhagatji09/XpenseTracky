package com.example.expensetracky.transactionfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracky.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DailyFragment extends Fragment implements AddTransactionFragment.OnTransactionAddedListener {

    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private List<Transaction> transactionsList;
    private FloatingActionButton fabAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_view_transactions);
        fabAdd = view.findViewById(R.id.fab_add);

        // Initialize transaction list and adapter
        transactionsList = new ArrayList<>();
        adapter = new TransactionAdapter(transactionsList);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Floating Action Button click listener to show AddTransactionFragment dialog
        fabAdd.setOnClickListener(v -> {
            AddTransactionFragment addTransactionFragment = new AddTransactionFragment();
            addTransactionFragment.setTargetFragment(DailyFragment.this, 0);  // Set the target fragment to DailyFragment
            addTransactionFragment.show(getParentFragmentManager(), "AddTransactionFragment");
        });

        return view;
    }

    @Override
    public void onTransactionAdded(Transaction transaction) {
        // Add the transaction to the list and update the RecyclerView
        transactionsList.add(transaction);
        adapter.notifyDataSetChanged();
    }
}
