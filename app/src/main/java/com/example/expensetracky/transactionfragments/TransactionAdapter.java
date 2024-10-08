package com.example.expensetracky.transactionfragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracky.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<Transaction> transactions;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.dayNumber.setText(transaction.getDayNumber());
        holder.dayOfWeek.setText(transaction.getDayOfWeek());
        holder.fullDate.setText(transaction.getFullDate());
        holder.expenseAmount.setText("₹"+transaction.getAmount().toString());
        holder.accountName.setText(transaction.getAccount());
        holder.categoryName.setText(transaction.getCategory());

        // Bind the notes to the TextView
        holder.notes.setText(transaction.getNotes());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView dayNumber, dayOfWeek, fullDate;
        TextView expenseAmount;
        TextView categoryName, accountName;
        TextView notes;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Bind the TextViews from the XML to Java
            dayNumber = itemView.findViewById(R.id.tv_day_number);
            dayOfWeek = itemView.findViewById(R.id.tv_day_of_week);
            fullDate = itemView.findViewById(R.id.tv_full_date);
            expenseAmount = itemView.findViewById(R.id.tv_total_expense);
            categoryName = itemView.findViewById(R.id.tv_category_name);
            accountName = itemView.findViewById(R.id.tv_account_name);
            notes = itemView.findViewById(R.id.tv_notes);  // Reference to notes TextView
        }
    }
}