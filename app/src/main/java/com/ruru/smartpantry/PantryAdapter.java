package com.ruru.smartpantry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/** Displays pantry ingredients in a simple ListView. */
public class PantryAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private final NumberFormat quantityFormat;
    private final List<PantryItem> pantryItems = new ArrayList<>();

    /** Creates an adapter for pantry ingredients. */
    public PantryAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        quantityFormat = NumberFormat.getNumberInstance();
        quantityFormat.setMaximumFractionDigits(2);
    }

    /** Replaces the displayed ingredients. */
    public void replaceItems(List<PantryItem> updatedPantryItems) {
        pantryItems.clear();
        pantryItems.addAll(updatedPantryItems);
        notifyDataSetChanged();
    }

    /** Returns the number of displayed ingredients. */
    @Override
    public int getCount() {
        return pantryItems.size();
    }

    /** Returns the ingredient at the requested position. */
    @Override
    public PantryItem getItem(int position) {
        return pantryItems.get(position);
    }

    /** Returns the database identifier for an ingredient. */
    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    /** Creates or updates one visible pantry row. */
    @Override
    public View getView(int position, View existingView, ViewGroup parent) {
        View itemView = existingView;
        if (itemView == null) {
            itemView = layoutInflater.inflate(R.layout.item_pantry, parent, false);
        }

        PantryItem pantryItem = getItem(position);
        TextView nameTextView = itemView.findViewById(R.id.ingredientNameTextView);
        TextView quantityTextView = itemView.findViewById(R.id.ingredientQuantityTextView);
        nameTextView.setText(pantryItem.getName());
        String quantity = quantityFormat.format(pantryItem.getQuantity());
        quantityTextView.setText(itemView.getContext().getString(
                R.string.quantity_and_unit, quantity, pantryItem.getUnit()));
        return itemView;
    }
}
