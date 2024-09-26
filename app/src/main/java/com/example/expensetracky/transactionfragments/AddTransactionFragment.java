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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AddTransactionFragment extends DialogFragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private EditText etDate, etAmount, etNotes;
    private Spinner etCategory, etAccount; // Ensure both Spinners are declared
    private Button btnSave;
    private OnTransactionAddedListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);

        etDate = view.findViewById(R.id.et_date);
        etAmount = view.findViewById(R.id.et_amount);
        etCategory = view.findViewById(R.id.spinner_category);
        etAccount = view.findViewById(R.id.spinner_account); // Initialize the account Spinner
        etNotes = view.findViewById(R.id.et_notes);
        btnSave = view.findViewById(R.id.btn_save);

        // Set a click listener on the date EditText to open the date picker
        etDate.setOnClickListener(v -> showDatePickerDialog());

        btnSave.setOnClickListener(v -> {
            Double amount;
            try {
                amount = Double.valueOf(etAmount.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid amount", Toast.LENGTH_SHORT).show();
                return; // Exit early if validation fails
            }

            String category = etCategory.getSelectedItem() != null ? etCategory.getSelectedItem().toString().trim() : "";
            String notes = etNotes.getText().toString().trim();
            String account = etAccount.getSelectedItem() != null ? etAccount.getSelectedItem().toString().trim() : "";

            if (category.isEmpty()) {
                Toast.makeText(getContext(), "Category is required", Toast.LENGTH_SHORT).show();
                return; // Exit early if validation fails
            }

            String date = etDate.getText().toString();
            Transaction transaction = new Transaction(date, amount, category, account, notes);

            // Put the transaction data in a Bundle and set it as a result
            Bundle result = new Bundle();
            result.putParcelable("transaction", transaction);
            getParentFragmentManager().setFragmentResult("transactionKey", result);

            // Close the dialog
            dismiss();
        });

        return view;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Handle the selected date here
                    String selectedDate = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    etDate.setText(selectedDate); // Set the date into the EditText
                },
                year, month, day
        );

        // Show the dialog
        datePickerDialog.show();
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
