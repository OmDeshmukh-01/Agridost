package com.example.dummy.community;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dummy.R;

import java.util.List;

public class FarmerSchemeAdapter extends RecyclerView.Adapter<FarmerSchemeAdapter.SchemeViewHolder> {
    private List<FarmerScheme> schemes;
    private Context context;

    public FarmerSchemeAdapter(Context context, List<FarmerScheme> schemes) {
        this.context = context;
        this.schemes = schemes;
    }

    @NonNull
    @Override
    public SchemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_farmer_scheme, parent, false);
        return new SchemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchemeViewHolder holder, int position) {
        FarmerScheme scheme = schemes.get(position);
        
        holder.schemeNumber.setText(String.valueOf(position + 1) + "️⃣");
        holder.schemeTitle.setText(scheme.getTitle());
        holder.schemeAmount.setText(scheme.getAmount());
        holder.schemeStatus.setText(scheme.getStatus());
        
        // Set status color
        if (scheme.getStatus().equals("Active")) {
            holder.schemeStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else if (scheme.getStatus().equals("Upcoming")) {
            holder.schemeStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            holder.schemeStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
        
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SchemeDetailActivity.class);
            intent.putExtra("scheme", scheme);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return schemes.size();
    }

    public void updateSchemes(List<FarmerScheme> newSchemes) {
        this.schemes = newSchemes;
        notifyDataSetChanged();
    }

    static class SchemeViewHolder extends RecyclerView.ViewHolder {
        TextView schemeNumber;
        TextView schemeTitle;
        TextView schemeAmount;
        TextView schemeStatus;

        public SchemeViewHolder(@NonNull View itemView) {
            super(itemView);
            schemeNumber = itemView.findViewById(R.id.scheme_number);
            schemeTitle = itemView.findViewById(R.id.scheme_title);
            schemeAmount = itemView.findViewById(R.id.scheme_amount);
            schemeStatus = itemView.findViewById(R.id.scheme_status);
        }
    }
}
