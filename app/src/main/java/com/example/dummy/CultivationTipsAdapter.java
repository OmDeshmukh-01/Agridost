package com.example.dummy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CultivationTipsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> cropGroups;
    private HashMap<String, List<CultivationTipsActivity.CultivationTip>> tipsData;

    public CultivationTipsAdapter(Context context, List<String> cropGroups, 
                                 HashMap<String, List<CultivationTipsActivity.CultivationTip>> tipsData) {
        this.context = context;
        this.cropGroups = cropGroups;
        this.tipsData = tipsData;
    }

    @Override
    public int getGroupCount() {
        return cropGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return tipsData.get(cropGroups.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return cropGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return tipsData.get(cropGroups.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String cropGroup = (String) getGroup(groupPosition);
        
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_cultivation, null);
        }
        
        TextView tvGroup = convertView.findViewById(R.id.tvGroup);
        tvGroup.setText(cropGroup);
        
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, 
                           View convertView, ViewGroup parent) {
        CultivationTipsActivity.CultivationTip tip = 
            (CultivationTipsActivity.CultivationTip) getChild(groupPosition, childPosition);
        
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_cultivation, null);
        }
        
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        
        tvTitle.setText(tip.getTitle());
        tvDescription.setText(tip.getDescription());
        
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
