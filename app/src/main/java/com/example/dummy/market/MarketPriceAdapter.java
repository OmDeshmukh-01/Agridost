package com.example.dummy.market;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dummy.R;

import java.util.List;

public class MarketPriceAdapter extends RecyclerView.Adapter<MarketPriceAdapter.ViewHolder> {

    private List<MarketPriceItem> priceList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MarketPriceItem item);
    }

    public MarketPriceAdapter(List<MarketPriceItem> priceList, OnItemClickListener listener) {
        this.priceList = priceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_market_price, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < priceList.size()) {
            MarketPriceItem item = priceList.get(position);
            
            holder.cropImage.setImageResource(item.getImageResource());
            holder.cropName.setText(item.getCropName());
            holder.marketLocation.setText(item.getMarketLocation());
            holder.price.setText(item.getPrice());
            holder.priceChange.setText(item.getPriceChange());
            
            // Set price change color and icon
            if (item.isPriceUp()) {
                holder.priceChange.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
                holder.trendIcon.setImageResource(R.drawable.ic_trending_up);
                holder.trendIcon.setColorFilter(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
            } else {
                holder.priceChange.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
                holder.trendIcon.setImageResource(R.drawable.ic_trending_down);
                holder.trendIcon.setColorFilter(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            }
            
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return priceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cropImage;
        TextView cropName;
        TextView marketLocation;
        TextView price;
        TextView priceChange;
        ImageView trendIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cropImage = itemView.findViewById(R.id.crop_image);
            cropName = itemView.findViewById(R.id.crop_name);
            marketLocation = itemView.findViewById(R.id.market_location);
            price = itemView.findViewById(R.id.price);
            priceChange = itemView.findViewById(R.id.price_change);
            trendIcon = itemView.findViewById(R.id.trend_icon);
        }
    }
}
