package com.example.expensetracky.transactionfragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.expensetracky.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AddNotesTransactionFragment extends DialogFragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private EditText title, note, date;
    private Button btnSaveNote;
    private OnNotesAddedListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_notes, container, false);

        title = view.findViewById(R.id.title);
        note = view.findViewById(R.id.note);
        date = view.findViewById(R.id.date);
        btnSaveNote = view.findViewById(R.id.buttonSaveNote);

        // Date picker logic
        date.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view1, year1, month1, dayOfMonth) -> {
                String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                date.setText(selectedDate);
            }, year, month, day);
            datePickerDialog.show();
        });

        btnSaveNote.setOnClickListener(v -> {
            String ettitle = title.getText().toString().trim();
            String noted = note.getText().toString().trim();
            String etdate = date.getText().toString().trim();

            Notes notes = new Notes(ettitle, noted, etdate);

            // Check if user is authenticated
            if (currentUser != null) {
                String userId = currentUser.getUid();

                // Now use this userId to access the Firestore collection
                db.collection("users").document(userId)
                        .collection("notes")
                        .add(notes)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Note added successfully", Toast.LENGTH_SHORT).show();

                            // Pass the note data back
                            Bundle result = new Bundle();
                            result.putParcelable("note", notes);
                            getParentFragmentManager().setFragmentResult("noteKey", result);

                            dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Error adding note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnNotesAddedListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnTransactionAddedListener");
        }
    }

    public interface OnNotesAddedListener {
        void onNoteAdded(Notes note);
    }
}
