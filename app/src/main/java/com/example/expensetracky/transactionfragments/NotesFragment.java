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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotesFragment extends Fragment implements AddNotesTransactionFragment.OnNotesAddedListener {
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private List<Notes> notesList;
    private FloatingActionButton fabAdd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("noteKey", this, (requestKey, bundle) -> {
            Notes note = bundle.getParcelable("note");
            if (note != null) {
                onNoteAdded(note);
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
        notesList = new ArrayList<>();
        adapter = new NoteAdapter(notesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Load user's notes
        loadNotes();

        // Floating Action Button click listener
        fabAdd.setOnClickListener(v -> {
            AddNotesTransactionFragment addNotesFragment = new AddNotesTransactionFragment();
            addNotesFragment.setTargetFragment(NotesFragment.this, 0);
            addNotesFragment.show(getParentFragmentManager(), "AddNotesTransactionFragment");
        });

        return view;
    }

    private void loadNotes() {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch notes from Firestore
            db.collection("users").document(userId)
                    .collection("notes") // Corrected to "notes"
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            notesList.clear(); // Clear the list before adding new items
                            for (DocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String noteContent = document.getString("note");
                                String date = document.getString("date");

                                if (title != null && noteContent != null && date != null) {
                                    Notes note = new Notes(title, noteContent, date);
                                    notesList.add(note);
                                }
                            }
                            adapter.notifyDataSetChanged(); // Notify adapter about data change
                        } else {
                            Toast.makeText(getContext(), "Failed to load notes.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void addNoteToFirestore(Notes note) {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Prepare the note data as a map
            Map<String, Object> noteData = new HashMap<>();
            noteData.put("title", note.getTitle());
            noteData.put("note", note.getNote());
            noteData.put("date", note.getDate()); // Add the date field

            // Add note to Firestore
            db.collection("users").document(userId)
                    .collection("notes") // Corrected to "notes"
                    .add(noteData)
                    .addOnSuccessListener(documentReference -> {
                        // Note added successfully
                        Toast.makeText(getContext(), "Note added!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure to add note
                        Toast.makeText(getContext(), "Failed to add note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public void onNoteAdded(Notes note) {
        notesList.add(note);

        // Notify the adapter about the change
        adapter.notifyItemInserted(notesList.size() - 1);

        // Scroll to the bottom of the list
        recyclerView.scrollToPosition(notesList.size() - 1);

        // Add the new note to Firestore
        addNoteToFirestore(note);
    }
}
