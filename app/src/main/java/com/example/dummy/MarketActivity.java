package com.example.dummy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class MarketActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MarketPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        
        try {
            initializeViews();
            setupViewPager();
            setupBottomNavigation();
        } catch (Exception e) {
            e.printStackTrace();
            // Show error message and finish activity
            Toast.makeText(this, "Error loading market module", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Ensure Market tab is selected when activity resumes
        BottomNavigationView bottomNav = findViewById(R.id.market_bottom_nav);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_market);
        }
    }
    
    private void initializeViews() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        
        if (tabLayout == null || viewPager == null) {
            throw new RuntimeException("Required views not found in layout");
        }
    }
    
    private void setupViewPager() {
        try {
            pagerAdapter = new MarketPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(pagerAdapter);
            viewPager.setOffscreenPageLimit(3);
            tabLayout.setupWithViewPager(viewPager);

            // Ensure icons are shown (titles come from adapter)
            if (tabLayout.getTabCount() >= 3) {
                if (tabLayout.getTabAt(0) != null) tabLayout.getTabAt(0).setIcon(R.drawable.ic_trending_up);
                if (tabLayout.getTabAt(1) != null) tabLayout.getTabAt(1).setIcon(R.drawable.ic_shopping_cart);
                if (tabLayout.getTabAt(2) != null) tabLayout.getTabAt(2).setIcon(R.drawable.ic_sell);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupBottomNavigation() {
        try {
            BottomNavigationView bottomNav = findViewById(R.id.market_bottom_nav);
            if (bottomNav != null) {
                bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int itemId = item.getItemId();
                        
                        if (itemId == R.id.nav_crops) {
                            Intent intent = new Intent(MarketActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return true;
                        } else if (itemId == R.id.nav_community) {
                            Intent intent = new Intent(MarketActivity.this, CommunityActivity.class);
                            startActivity(intent);
                            finish();
                            return true;
                        } else if (itemId == R.id.nav_market) {
                            // Already on market page - do nothing, just return true
                            // This should prevent any navigation away from MarketActivity
                            return true;
                        } else if (itemId == R.id.nav_you) {
                            Intent intent = new Intent(MarketActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }
                        
                        return false;
                    }
                });
                
                // Set Market as selected
                bottomNav.setSelectedItemId(R.id.nav_market);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static class MarketPagerAdapter extends FragmentPagerAdapter {
        
        public MarketPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        
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
