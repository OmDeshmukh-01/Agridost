package com.example.dummy.market;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dummy.ProductItem;
import com.example.dummy.R;

import java.util.ArrayList;
import java.util.List;

public class SellProductsFragment extends Fragment {

    private RecyclerView productGrid;
    private TextView headerTitle;
    private ProductAdapter productAdapter;
    private List<ProductItem> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_products, container, false);

        headerTitle = view.findViewById(R.id.header_title);
        productGrid = view.findViewById(R.id.sell_product_grid);

        headerTitle.setText("Sell Products (Static)");

        productList = new ArrayList<>();
        // Static sample products for selling UI
        productList.add(new ProductItem("Wheat (Sell)", "Your Farm", "₹2,250/Q", "100 Q available", R.drawable.wheat, "Sell"));
        productList.add(new ProductItem("Maize (Sell)", "Your Farm", "₹1,680/Q", "70 Q available", R.drawable.maize, "Sell"));
        productList.add(new ProductItem("Potato (Sell)", "Your Farm", "₹28/kg", "500 kg available", R.drawable.potato, "Sell"));

        productAdapter = new ProductAdapter(productList, product -> {});
        productGrid.setLayoutManager(new GridLayoutManager(getContext(), 1));
        productGrid.setAdapter(productAdapter);

        return view;
    }
}
