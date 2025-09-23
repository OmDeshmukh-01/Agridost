package com.example.dummy.market;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dummy.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private List<CategoryItem> categoryList;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(CategoryItem category);
    }

    public CategoryAdapter(List<CategoryItem> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position < categoryList.size()) { // Safety check
            CategoryItem category = categoryList.get(position);

            holder.categoryIcon.setImageResource(category.getIconResource());
            holder.categoryName.setText(category.getName());

            // Set background color
            try {
                int color = Color.parseColor(category.getBackgroundColor());
                holder.categoryCard.setCardBackgroundColor(color);
            } catch (IllegalArgumentException e) {
                holder.categoryCard.setCardBackgroundColor(Color.parseColor("#F5F5F5")); // Default fallback color
            }

            convertView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(category);
                }
            });
        }
        return convertView;
    }

    public static class ViewHolder {
        MaterialCardView categoryCard;
        ImageView categoryIcon;
        TextView categoryName;

        public ViewHolder(View view) {
            categoryCard = view.findViewById(R.id.category_card);
            categoryIcon = view.findViewById(R.id.category_icon);
            categoryName = view.findViewById(R.id.category_name);
        }
    }
}