package com.example.expensetracky.transactionfragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracky.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Notes> notesList;

    public NoteAdapter(List<Notes> notes) {
        this.notesList = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notes, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Notes singlenote = notesList.get(position);
        holder.title.setText(singlenote.getTitle());
        holder.notes.setText(singlenote.getNote());
        holder.date.setText(singlenote.getDate());  // Display the date
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView notes, title, date;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_note_title);
            notes = itemView.findViewById(R.id.tv_note_content);  // Correct ID
            date = itemView.findViewById(R.id.tv_note_date); // Reference to the date TextView
        }
    }
}
