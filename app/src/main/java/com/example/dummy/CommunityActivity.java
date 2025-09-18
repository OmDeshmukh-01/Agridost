package com.example.dummy;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity implements AskCommunityDialog.OnPostCreatedListener, CommentDialog.OnCommentAddedListener {
    
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private ChipGroup chipGroupFilters;
    private TextInputLayout searchInput;
    private FloatingActionButton fabAskCommunity;
    private MaterialToolbar toolbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_community);
        
        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupFilters();
        setupSearch();
        setupFloatingActionButton();
        loadSampleData();
    }
    
    private void initializeViews() {
        recyclerViewPosts = findViewById(R.id.recycler_view_posts);
        chipGroupFilters = findViewById(R.id.chip_group_filters);
        searchInput = findViewById(R.id.search_input);
        fabAskCommunity = findViewById(R.id.fab_ask_community);
        toolbar = findViewById(R.id.toolbar);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Community");
        }
        
        // Setup notification bell
        ImageView notificationBell = findViewById(R.id.notification_bell);
        notificationBell.setOnClickListener(v -> {
            // Handle notification click
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show();
        });
        
        // Setup menu dots
        ImageView menuDots = findViewById(R.id.menu_dots);
        menuDots.setOnClickListener(v -> {
            // Handle menu click
            Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void setupRecyclerView() {
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, this);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPosts.setAdapter(postAdapter);
    }
    
    private void setupFilters() {
        // Add filter chips
        String[] cropTypes = {"Pea", "Cotton", "Tomato", "Onion", "Wheat", "Rice", "Potato", "Corn", "Soybean", "Sugarcane"};
        int[] cropIcons = {R.drawable.ic_pea, R.drawable.ic_cotton, R.drawable.ic_tomato, 
                          R.drawable.ic_onion, R.drawable.ic_wheat, R.drawable.ic_rice,
                          R.drawable.ic_potato, R.drawable.ic_corn, R.drawable.ic_soybean, R.drawable.ic_sugarcane};
        
        for (int i = 0; i < cropTypes.length; i++) {
            Chip chip = new Chip(this);
            chip.setText(cropTypes[i]);
            chip.setChipIcon(getDrawable(cropIcons[i]));
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    filterPostsByCrop(buttonView.getText().toString());
                } else {
                    loadAllPosts();
                }
            });
            chipGroupFilters.addView(chip);
        }
    }
    
    private void setupSearch() {
        searchInput.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            String searchQuery = v.getText().toString().trim();
            if (!searchQuery.isEmpty()) {
                searchPosts(searchQuery);
            } else {
                loadAllPosts();
            }
            return true;
        });
    }
    
    private void setupFloatingActionButton() {
        fabAskCommunity.setOnClickListener(v -> {
            // Open ask community dialog
            AskCommunityDialog dialog = new AskCommunityDialog();
            dialog.show(getSupportFragmentManager(), "AskCommunityDialog");
        });
    }
    
    private void loadSampleData() {
        postList.clear();
        
        // Sample post 1
        Post post1 = new Post();
        post1.setId("1");
        post1.setTitle("Tomato is west");
        post1.setContent("All Fruits are west, black transparent");
        post1.setAuthorName("Nilesh Jadhav");
        post1.setAuthorLocation("India");
        post1.setCropType("Tomato");
        post1.setTimeAgo("2 h");
        post1.setImageUrl("https://example.com/tomato.jpg");
        post1.setLikesCount(0);
        post1.setDislikesCount(0);
        post1.setCommentsCount(0);
        postList.add(post1);
        
        // Sample post 2
        Post post2 = new Post();
        post2.setId("2");
        post2.setTitle("Pea plant disease");
        post2.setContent("My pea plants are showing yellow spots on leaves. What could be the cause?");
        post2.setAuthorName("Priya Sharma");
        post2.setAuthorLocation("Pune, India");
        post2.setCropType("Pea");
        post2.setTimeAgo("4 h");
        post2.setImageUrl("https://example.com/pea.jpg");
        post2.setLikesCount(3);
        post2.setDislikesCount(0);
        post2.setCommentsCount(2);
        postList.add(post2);
        
        // Sample post 3
        Post post3 = new Post();
        post3.setId("3");
        post3.setTitle("Cotton yield optimization");
        post3.setContent("Looking for advice on increasing cotton yield in dry conditions");
        post3.setAuthorName("Rajesh Kumar");
        post3.setAuthorLocation("Gujarat, India");
        post3.setCropType("Cotton");
        post3.setTimeAgo("1 d");
        post3.setImageUrl("https://example.com/cotton.jpg");
        post3.setLikesCount(5);
        post3.setDislikesCount(1);
        post3.setCommentsCount(4);
        postList.add(post3);
        
        // Sample post 4
        Post post4 = new Post();
        post4.setId("4");
        post4.setTitle("Wheat rust problem");
        post4.setContent("My wheat crop is affected by rust. Need urgent help with treatment options.");
        post4.setAuthorName("Amit Singh");
        post4.setAuthorLocation("Punjab, India");
        post4.setCropType("Wheat");
        post4.setTimeAgo("6 h");
        post4.setImageUrl("https://example.com/wheat.jpg");
        post4.setLikesCount(8);
        post4.setDislikesCount(0);
        post4.setCommentsCount(6);
        postList.add(post4);
        
        // Sample post 5
        Post post5 = new Post();
        post5.setId("5");
        post5.setTitle("Rice irrigation schedule");
        post5.setContent("What's the best irrigation schedule for rice during summer months?");
        post5.setAuthorName("Sunita Reddy");
        post5.setAuthorLocation("Andhra Pradesh, India");
        post5.setCropType("Rice");
        post5.setTimeAgo("8 h");
        post5.setImageUrl("https://example.com/rice.jpg");
        post5.setLikesCount(4);
        post5.setDislikesCount(0);
        post5.setCommentsCount(3);
        postList.add(post5);
        
        // Sample post 6
        Post post6 = new Post();
        post6.setId("6");
        post6.setTitle("Onion storage tips");
        post6.setContent("How to store onions properly to prevent sprouting and rotting?");
        post6.setAuthorName("Vikram Patel");
        post6.setAuthorLocation("Maharashtra, India");
        post6.setCropType("Onion");
        post6.setTimeAgo("12 h");
        post6.setImageUrl("https://example.com/onion.jpg");
        post6.setLikesCount(7);
        post6.setDislikesCount(0);
        post6.setCommentsCount(5);
        postList.add(post6);
        
        // Sample post 7
        Post post7 = new Post();
        post7.setId("7");
        post7.setTitle("Sugarcane pest control");
        post7.setContent("Red rot disease in sugarcane - need organic treatment methods");
        post7.setAuthorName("Deepak Verma");
        post7.setAuthorLocation("Uttar Pradesh, India");
        post7.setCropType("Sugarcane");
        post7.setTimeAgo("1 d");
        post7.setImageUrl("https://example.com/sugarcane.jpg");
        post7.setLikesCount(12);
        post7.setDislikesCount(1);
        post7.setCommentsCount(8);
        postList.add(post7);
        
        // Sample post 8
        Post post8 = new Post();
        post8.setId("8");
        post8.setTitle("Potato farming in hills");
        post8.setContent("Best potato varieties for hilly regions with cold climate");
        post8.setAuthorName("Ravi Thapa");
        post8.setAuthorLocation("Himachal Pradesh, India");
        post8.setCropType("Potato");
        post8.setTimeAgo("2 d");
        post8.setImageUrl("https://example.com/potato.jpg");
        post8.setLikesCount(6);
        post8.setDislikesCount(0);
        post8.setCommentsCount(4);
        postList.add(post8);
        
        // Sample post 9
        Post post9 = new Post();
        post9.setId("9");
        post9.setTitle("Corn fertilizer timing");
        post9.setContent("When is the best time to apply fertilizer for corn crops?");
        post9.setAuthorName("Suresh Kumar");
        post9.setAuthorLocation("Haryana, India");
        post9.setCropType("Corn");
        post9.setTimeAgo("3 d");
        post9.setImageUrl("https://example.com/corn.jpg");
        post9.setLikesCount(9);
        post9.setDislikesCount(0);
        post9.setCommentsCount(7);
        postList.add(post9);
        
        // Sample post 10
        Post post10 = new Post();
        post10.setId("10");
        post10.setTitle("Soybean market prices");
        post10.setContent("Current soybean prices in local market are very low. Any suggestions?");
        post10.setAuthorName("Meera Joshi");
        post10.setAuthorLocation("Madhya Pradesh, India");
        post10.setCropType("Soybean");
        post10.setTimeAgo("4 d");
        post10.setImageUrl("https://example.com/soybean.jpg");
        post10.setLikesCount(15);
        post10.setDislikesCount(2);
        post10.setCommentsCount(12);
        postList.add(post10);
        
        postAdapter.notifyDataSetChanged();
    }
    
    private void filterPostsByCrop(String cropType) {
        List<Post> filteredPosts = new ArrayList<>();
        for (Post post : postList) {
            if (post.getCropType().equalsIgnoreCase(cropType)) {
                filteredPosts.add(post);
            }
        }
        postAdapter.updatePosts(filteredPosts);
    }
    
    private void searchPosts(String query) {
        List<Post> searchResults = new ArrayList<>();
        for (Post post : postList) {
            if (post.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                post.getContent().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(post);
            }
        }
        postAdapter.updatePosts(searchResults);
    }
    
    private void loadAllPosts() {
        postAdapter.updatePosts(postList);
    }
    
    public void onPostLike(Post post) {
        post.setLikesCount(post.getLikesCount() + 1);
        postAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Post liked!", Toast.LENGTH_SHORT).show();
    }
    
    public void onPostDislike(Post post) {
        post.setDislikesCount(post.getDislikesCount() + 1);
        postAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Post disliked!", Toast.LENGTH_SHORT).show();
    }
    
    public void onPostComment(Post post) {
        CommentDialog dialog = CommentDialog.newInstance(post);
        dialog.show(getSupportFragmentManager(), "CommentDialog");
    }
    
    public void onPostShare(Post post) {
        // Handle sharing functionality
        Toast.makeText(this, "Post shared!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    @Override
    public void onPostCreated(Post post) {
        postList.add(0, post); // Add to beginning of list
        postAdapter.notifyItemInserted(0);
        recyclerViewPosts.smoothScrollToPosition(0);
    }
    
    @Override
    public void onCommentAdded(Comment comment) {
        // Find the post and update its comment count
        for (Post post : postList) {
            if (post.getId().equals(comment.getPostId())) {
                post.setCommentsCount(post.getCommentsCount() + 1);
                postAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
