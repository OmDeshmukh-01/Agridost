package com.example.dummy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPosts;
    private CommunityPostAdapter postAdapter;
    private List<CommunityPost> postsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_community);

        setupToolbar();
        setupFilterChips();
        setupRecyclerView();
        setupAskCommunityButton();
        setupBottomNavigation();
    }

    private void setupToolbar() {
        ImageView notificationIcon = findViewById(R.id.act_com_notification_icon);
        ImageView menuIcon = findViewById(R.id.act_com_menu_icon);
        
        notificationIcon.setOnClickListener(v -> {
            // Handle notification click
        });
        
        menuIcon.setOnClickListener(v -> {
            // Handle menu click
        });
    }

    private void setupFilterChips() {
        ChipGroup chipGroup = findViewById(R.id.act_com_filter_chips);
        
        String[] crops = {"Pea", "Cotton", "Tomato", "Onion"};
        for (String crop : crops) {
            Chip chip = new Chip(this);
            chip.setText(crop);
            chip.setCheckable(true);
            chip.setChecked(crop.equals("Tomato")); // Tomato selected by default
            chipGroup.addView(chip);
        }
    }

    private void setupRecyclerView() {
        recyclerViewPosts = findViewById(R.id.act_com_recycler_posts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
        
        postsList = createSamplePosts();
        postAdapter = new CommunityPostAdapter(postsList, this);
        recyclerViewPosts.setAdapter(postAdapter);
    }

    private void setupAskCommunityButton() {
        MaterialButton askButton = findViewById(R.id.act_com_button_ask);
        askButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AskCommunityActivity.class);
            startActivity(intent);
        });
    }

    private List<CommunityPost> createSamplePosts() {
        List<CommunityPost> posts = new ArrayList<>();
        
        CommunityPost post = new CommunityPost();
        post.setUserName("Nilesh Jadhav");
        post.setUserLocation("India");
        post.setTimeAgo("2 h");
        post.setCropType("Tomato");
        post.setTitle("Tomato is west");
        post.setDescription("All Fruits are west, black transparent");
        post.setImageResource(R.drawable.tomato_sample);
        post.setLikesCount(0);
        post.setDislikesCount(0);
        post.setAnswersCount(0);
        
        posts.add(post);
        return posts;
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.act_com_bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_crops) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_community) {
                // Already on community page
                return true;
            } else if (itemId == R.id.nav_market) {
                // TODO: Implement Market activity
                return true;
            } else if (itemId == R.id.nav_you) {
                // TODO: Implement Profile activity
                return true;
            }
            return false;
        });
        
        // Set Community as selected
        bottomNav.setSelectedItemId(R.id.nav_community);
    }
}
