package com.example.dummy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<ProductItem> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(ProductItem product);
    }

    public ProductAdapter(List<ProductItem> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < productList.size()) {
            ProductItem product = productList.get(position);
            
            holder.productImage.setImageResource(product.getImageResource());
            holder.productName.setText(product.getName());
            holder.productBrand.setText(product.getBrand());
            holder.productPrice.setText(product.getPrice());
            holder.productQuantity.setText(product.getQuantity());
            
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateProducts(List<ProductItem> newProducts) {
        this.productList = newProducts;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productBrand;
        TextView productPrice;
        TextView productQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productBrand = itemView.findViewById(R.id.product_brand);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
        }
    }
}