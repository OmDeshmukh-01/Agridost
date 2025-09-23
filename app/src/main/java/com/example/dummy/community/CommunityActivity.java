package com.example.dummy.community;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dummy.CommunityDatabase;
import com.example.dummy.CommunityPost;
import com.example.dummy.CommunityPostAdapter;
import com.example.dummy.Main.MainActivity;
import com.example.dummy.market.MarketActivity;
import com.example.dummy.Profile.ProfileActivity;
import com.example.dummy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CommunityActivity extends AppCompatActivity implements CommunityPostAdapter.OnPostInteractionListener {

    private RecyclerView recyclerViewPosts;
    private CommunityPostAdapter postAdapter;
    private ChipGroup filterChipGroup;
    private TextInputEditText searchEditText;
    private String currentFilter = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_community);

        setupToolbar();
        setupAskCommunityButton();
        setupBottomNavigation();
        setupRecyclerView();
        setupFilterChips();
        setupSearch();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh posts when returning from AskCommunityActivity
        loadPosts();
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

    private void setupAskCommunityButton() {
        MaterialButton askButton = findViewById(R.id.act_com_button_ask);
        askButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AskCommunityActivity.class);
            startActivity(intent);
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.act_com_bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_community) {
                // Already on community page
                return true;
            } else if (itemId == R.id.nav_crops) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_market) {
                Intent intent = new Intent(this, MarketActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_you) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            
            return false;
        });
        
        // Set Community as selected
        bottomNav.setSelectedItemId(R.id.nav_community);
    }
    
    private void setupRecyclerView() {
        recyclerViewPosts = findViewById(R.id.act_com_recycler_posts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
        
        List<CommunityPost> posts = CommunityDatabase.getAllPosts();
        postAdapter = new CommunityPostAdapter(this, posts, this);
        recyclerViewPosts.setAdapter(postAdapter);
    }
    
    private void setupFilterChips() {
        filterChipGroup = findViewById(R.id.act_com_filter_chips);
        
        // Add "All" chip
        Chip allChip = new Chip(this);
        allChip.setText("All");
        allChip.setChecked(true);
        allChip.setOnClickListener(v -> {
            currentFilter = "All";
            loadPosts();
        });
        filterChipGroup.addView(allChip, 0);
        
        // Set up existing chips
        for (int i = 1; i < filterChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) filterChipGroup.getChildAt(i);
            chip.setOnClickListener(v -> {
                currentFilter = chip.getText().toString();
                loadPosts();
            });
        }
    }
    
    private void setupSearch() {
        // Search functionality can be added here if needed
        // The search EditText is inside the TextInputLayout
    }
    
    private void loadPosts() {
        List<CommunityPost> posts;
        if (currentFilter.equals("All")) {
            posts = CommunityDatabase.getAllPosts();
        } else {
            posts = CommunityDatabase.getPostsByCropType(currentFilter);
        }
        
        if (postAdapter != null) {
            postAdapter.updatePosts(posts);
        }
    }
    
    // Implement OnPostInteractionListener methods
    @Override
    public void onLikeClick(CommunityPost post) {
        CommunityDatabase.likePost(post.getId());
        loadPosts(); // Refresh to show updated counts
        Toast.makeText(this, "Liked!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDislikeClick(CommunityPost post) {
        CommunityDatabase.dislikePost(post.getId());
        loadPosts(); // Refresh to show updated counts
        Toast.makeText(this, "Disliked!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onCommentClick(CommunityPost post) {
        CommunityDatabase.addComment(post.getId());
        loadPosts(); // Refresh to show updated counts
        Toast.makeText(this, "Comment added!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onShareClick(CommunityPost post) {
        // Create share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, post.getTitle() + "\n\n" + post.getDescription());
        startActivity(Intent.createChooser(shareIntent, "Share this post"));
    }
}