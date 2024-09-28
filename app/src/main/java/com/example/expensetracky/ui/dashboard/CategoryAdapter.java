package com.example.expensetracky.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracky.R;

import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<Map.Entry<String, Float>> categoryList;

    public CategoryAdapter(List<Map.Entry<String, Float>> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map.Entry<String, Float> categoryEntry = categoryList.get(position);
        holder.categoryName.setText(categoryEntry.getKey());
        holder.categoryTotal.setText(String.format("₹ %.2f", categoryEntry.getValue()));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        TextView categoryTotal;

        ViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.tv_category_name);
            categoryTotal = itemView.findViewById(R.id.tv_category_amount);
        }
    }
}

