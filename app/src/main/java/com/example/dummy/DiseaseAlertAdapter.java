package com.example.dummy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DiseaseAlertAdapter extends BaseAdapter {

    private Context context;
    private List<DiseaseAlertActivity.DiseaseAlert> alerts;

    public DiseaseAlertAdapter(Context context, List<DiseaseAlertActivity.DiseaseAlert> alerts) {
        this.context = context;
        this.alerts = alerts;
    }

    @Override
    public int getCount() {
        return alerts.size();
    }

    @Override
    public Object getItem(int position) {
        return alerts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_disease_alert, null);
        }

        DiseaseAlertActivity.DiseaseAlert alert = alerts.get(position);

        TextView tvSeverity = convertView.findViewById(R.id.tvSeverity);
        TextView tvCrop = convertView.findViewById(R.id.tvCrop);
        TextView tvRegion = convertView.findViewById(R.id.tvRegion);
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        TextView tvTimeAgo = convertView.findViewById(R.id.tvTimeAgo);

        tvSeverity.setText(alert.getSeverity());
        tvCrop.setText(alert.getCrop());
        tvRegion.setText(alert.getRegion());
        tvTitle.setText(alert.getTitle());
        tvDescription.setText(alert.getDescription());
        tvTimeAgo.setText(alert.getTimeAgo());

        // Set severity color
        switch (alert.getSeverity().toLowerCase()) {
            case "high":
                tvSeverity.setBackgroundColor(Color.parseColor("#F44336"));
                tvSeverity.setTextColor(Color.WHITE);
                break;
            case "medium":
                tvSeverity.setBackgroundColor(Color.parseColor("#FF9800"));
                tvSeverity.setTextColor(Color.WHITE);
                break;
            case "low":
                tvSeverity.setBackgroundColor(Color.parseColor("#4CAF50"));
                tvSeverity.setTextColor(Color.WHITE);
                break;
        }

        return convertView;
    }
}
