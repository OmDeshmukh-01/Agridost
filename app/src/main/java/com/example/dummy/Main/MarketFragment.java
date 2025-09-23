package com.example.dummy.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.dummy.R;
import com.example.dummy.market.BuyProductsFragment;
import com.example.dummy.market.MarketPricesFragment;
import com.example.dummy.market.SellProductsFragment;
import com.google.android.material.tabs.TabLayout;

public class MarketFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_market, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        setupViewPager();
        return view;
    }

    private void setupViewPager() {
        MarketPagerAdapter pagerAdapter = new MarketPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        if (tabLayout.getTabCount() >= 3) {
            if (tabLayout.getTabAt(0) != null) tabLayout.getTabAt(0).setIcon(R.drawable.ic_trending_up);
            if (tabLayout.getTabAt(1) != null) tabLayout.getTabAt(1).setIcon(R.drawable.ic_shopping_cart);
            if (tabLayout.getTabAt(2) != null) tabLayout.getTabAt(2).setIcon(R.drawable.ic_sell);
        }
    }

    private static class MarketPagerAdapter extends FragmentPagerAdapter {
        public MarketPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MarketPricesFragment();
                case 1:
                    return new BuyProductsFragment();
                case 2:
                    return new SellProductsFragment();
                default:
                    return new MarketPricesFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Market Prices";
                case 1:
                    return "Buy Products";
                case 2:
                    return "Sell Products";
                default:
                    return "";
            }
        }
    }
}
