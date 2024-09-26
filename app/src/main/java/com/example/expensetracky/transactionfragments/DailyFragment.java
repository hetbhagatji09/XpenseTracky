package com.example.expensetracky.transactionfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracky.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class DailyFragment extends Fragment implements AddTransactionFragment.OnTransactionAddedListener {
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private List<Transaction> transactionsList;
    private FloatingActionButton fabAdd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("transactionKey", this, (requestKey, bundle) -> {
            Transaction transaction = bundle.getParcelable("transaction");
            if (transaction != null) {
                onTransactionAdded(transaction);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_transactions);
        fabAdd = view.findViewById(R.id.fab_add);
        transactionsList = new ArrayList<>();
        adapter = new TransactionAdapter(transactionsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Load user's transactions
        loadTransactions();

        // Floating Action Button click listener
        fabAdd.setOnClickListener(v -> {
            AddTransactionFragment addTransactionFragment = new AddTransactionFragment();
            addTransactionFragment.setTargetFragment(DailyFragment.this, 0);
            addTransactionFragment.show(getParentFragmentManager(), "AddTransactionFragment");
        });

        return view;
    }

    private void loadTransactions() {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch transactions from Firestore
            db.collection("users").document(userId)
                    .collection("transactions")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                transactionsList.clear(); // Clear the list before adding new items
                                for (DocumentSnapshot document : task.getResult()) {
                                    String account = document.getString("account");
                                    Double amount = null;

                                    // Try to get the amount as a number, fallback to parsing if it's a string
                                    Object amountObj = document.get("amount");

                                    // Check if amount is a Number or String, and handle accordingly
                                    if (amountObj instanceof Number) {
                                        amount = ((Number) amountObj).doubleValue();
                                    } else if (amountObj instanceof String) {
                                        try {
                                            amount = Double.parseDouble((String) amountObj);
                                        } catch (NumberFormatException e) {
                                            // Handle parsing error, set amount to null
                                            amount = null;
                                        }
                                    }

                                    String category = document.getString("category");
                                    String date = document.getString("date");
                                    String notes = document.getString("notes");
                                    String dayOfWeekString = document.getString("dayOfWeek");


                                    // Create and add Transaction object only if amount is valid
                                    if (amount != null) {
                                        Transaction transaction = new Transaction(date, amount, category, account, notes ,dayOfWeekString);
                                        transactionsList.add(transaction);
                                    } else {
                                        // Handle invalid or missing amount field if necessary
                                        Toast.makeText(getContext(), "Invalid amount in transaction.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                adapter.notifyDataSetChanged(); // Notify adapter about data change
                            } else {
                                // No transactions found, handle this case
                                Toast.makeText(getContext(), "No transactions found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle failure to fetch transactions
                            Toast.makeText(getContext(), "Failed to load transactions.", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }



    @Override
    public void onTransactionAdded(Transaction transaction) {
        transactionsList.add(transaction);
         // Call method to add transaction to Firestore

        // Notify the adapter about the change
        adapter.notifyItemInserted(transactionsList.size() - 1);

        // Scroll to the bottom of the list
        recyclerView.scrollToPosition(transactionsList.size() - 1);
    }

    private void addTransactionToFirestore(Transaction transaction) {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Add transaction to Firestore
            db.collection("users").document(userId)
                    .collection("transactions")
                    .add(transaction)
                    .addOnSuccessListener(documentReference -> {
                        // Transaction added successfully
                        Toast.makeText(getContext(), "Transaction added!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure to add transaction
                        Toast.makeText(getContext(), "Failed to add transaction: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}