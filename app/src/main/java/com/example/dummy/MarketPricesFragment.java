package com.example.dummy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MarketPricesFragment extends Fragment {

    private RecyclerView recyclerView;
    private MarketPriceAdapter adapter;
    private List<MarketPriceItem> priceList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_prices, container, false);
        
        try {
            initializeViews(view);
            setupRecyclerView();
            loadMarketPrices();
        } catch (Exception e) {
            e.printStackTrace();
            // Return a simple view if there's an error
            return new View(getContext());
        }
        
        return view;
    }
    
    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_market_prices);
    }
    
    private void setupRecyclerView() {
        if (getContext() == null) return;
        
        priceList = new ArrayList<>();
        adapter = new MarketPriceAdapter(priceList, new MarketPriceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MarketPriceItem item) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Selected: " + item.getCropName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    
    private void loadMarketPrices() {
        // Add sample market price data
        priceList.clear();
        
        priceList.add(new MarketPriceItem(
            R.drawable.wheat,
            "Wheat",
            "City Crop Market",
            "₹2,250/Q",
            true,
            "+5.2%"
        ));
        
        priceList.add(new MarketPriceItem(
            R.drawable.maize,
            "Maize",
            "Lender Cart House",
            "₹1,680/Q",
            false,
            "-2.1%"
        ));
        
        priceList.add(new MarketPriceItem(
            R.drawable.bajra,
            "Bajra",
            "City Corp Market",
            "₹1,850/Q",
            true,
            "+3.8%"
        ));
        
        priceList.add(new MarketPriceItem(
            R.drawable.tomato,
            "Tomato",
            "Fresh Market",
            "₹45/kg",
            true,
            "+8.5%"
        ));
        
        priceList.add(new MarketPriceItem(
            R.drawable.onion,
            "Onion",
            "Vegetable Market",
            "₹35/kg",
            false,
            "-1.2%"
        ));
        
        priceList.add(new MarketPriceItem(
            R.drawable.potato,
            "Potato",
            "City Market",
            "₹28/kg",
            true,
            "+2.3%"
        ));
        
        priceList.add(new MarketPriceItem(
            R.drawable.cotton,
            "Cotton",
            "Textile Market",
            "₹6,500/Q",
            true,
            "+4.7%"
        ));
        
        priceList.add(new MarketPriceItem(
            R.drawable.sugercane,
            "Sugarcane",
            "Sugar Mill Market",
            "₹320/Q",
            false,
            "-0.8%"
        ));
        
        adapter.notifyDataSetChanged();
    }
}
