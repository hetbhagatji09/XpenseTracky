package com.example.expensetracky.transactionfragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.expensetracky.R;

import java.util.Calendar;

public class AddTransactionFragment extends DialogFragment {

    private EditText etDate, etAmount, etNotes;
    private Spinner etCategory, etAccount; // Use EditText for category and account
    private Button btnSave;
    private OnTransactionAddedListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);

        // Initialize the views
        etDate = view.findViewById(R.id.et_date);
        etAmount = view.findViewById(R.id.et_amount);
        etCategory = view.findViewById(R.id.spinner_account); // Correct initialization
        etAccount = view.findViewById(R.id.spinner_category); // Correct initialization
        etNotes = view.findViewById(R.id.et_notes);
        btnSave = view.findViewById(R.id.btn_save);

        // Set date picker
        etDate.setOnClickListener(v -> showDatePickerDialog());

        // Save button
        btnSave.setOnClickListener(v -> saveTransaction());

        return view;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etDate.setText(date); // Set the date in the EditText
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveTransaction() {
        String date = etDate.getText().toString();
        String amount = etAmount.getText().toString();
        String category = etCategory.getSelectedItem().toString();
        String account = etAccount.getSelectedItem().toString();
        String notes = etNotes.getText().toString();

        // Validate inputs
        if (date.isEmpty() || amount.isEmpty() || category.isEmpty() || account.isEmpty()) {
            if (date.isEmpty()) {
                etDate.setError("Date is required");
            }
            if (amount.isEmpty()) {
                etAmount.setError("Amount is required");
            }

            return; // Exit the method if validation fails
        }

        // Create a Transaction object
        Transaction transaction = new Transaction(date, amount, category, account, notes);

        // Notify the listener (DailyFragment)
        if (listener != null) {
            listener.onTransactionAdded(transaction);
        }

        // Close the dialog
        dismiss();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnTransactionAddedListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnTransactionAddedListener");
        }
    }

    // Interface to pass transaction data to DailyFragment
    public interface OnTransactionAddedListener {
        void onTransactionAdded(Transaction transaction);
    }
}
