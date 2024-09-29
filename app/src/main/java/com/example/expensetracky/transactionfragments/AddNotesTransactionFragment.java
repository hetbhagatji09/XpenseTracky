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
import java.util.HashMap;
import java.util.Map;

public class AddNotesTransactionFragment extends DialogFragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private EditText title,note;
    private Button btnSaveNote;
    private OnNotesAddedListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_notes, container, false);

        btnSaveNote=view.findViewById(R.id.buttonSaveNote);
        // Set a click listener on the date EditText to open the date picker
        title = view.findViewById(R.id.title); // Initialize with the correct ID from your layout
        note = view.findViewById(R.id.note);

        btnSaveNote.setOnClickListener(v -> {
            currentUser = FirebaseAuth.getInstance().getCurrentUser();

           String ettitle=title.getText().toString().trim();
           String noted=note.getText().toString().trim();


            // Create the Notes object
            Notes notes = new Notes(ettitle,noted);

            // Check if user is authenticated
            if (currentUser != null) {
                String userId = currentUser.getUid(); // Get the authenticated user's UID

                // Now use this userId to access the Firestore collection
                db.collection("users").document(userId)
                        .collection("notes")
                        .add(notes) // Firestore auto-generates a document ID
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Note added successfully", Toast.LENGTH_SHORT).show();

                            // Prepare the result bundle with the note
                            Bundle result = new Bundle();
                            result.putParcelable("note", notes);
                            getParentFragmentManager().setFragmentResult("noteKey", result);

                            // Close the dialog
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

    // Interface to pass transaction data to DailyFragment
    public interface OnNotesAddedListener {
        void onNoteAdded(Notes note);
    }
}