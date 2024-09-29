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

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {


        TextView notes,title;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            // Bind the TextViews from the XML to Java
            title=itemView.findViewById(R.id.noteTitle);
            notes=itemView.findViewById(R.id.noteDescription);// Reference to notes TextView
        }
    }
}