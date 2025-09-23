package com.example.dummy.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dummy.CommunityDatabase;
import com.example.dummy.CommunityPost;
import com.example.dummy.CommunityPostAdapter;
import com.example.dummy.R;
import com.example.dummy.community.AskCommunityActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CommunityFragment extends Fragment implements CommunityPostAdapter.OnPostInteractionListener {

    private RecyclerView recyclerViewPosts;
    private CommunityPostAdapter postAdapter;
    private ChipGroup filterChipGroup;
    private TextInputEditText searchEditText;
    private String currentFilter = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_community, container, false);
        setupToolbar(view);
        setupAskCommunityButton(view);
        setupRecyclerView(view);
        setupFilterChips(view);
        setupSearch(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPosts();
    }

    private void setupToolbar(View root) {
        ImageView notificationIcon = root.findViewById(R.id.act_com_notification_icon);
        ImageView menuIcon = root.findViewById(R.id.act_com_menu_icon);
        notificationIcon.setOnClickListener(v -> {});
        menuIcon.setOnClickListener(v -> {});
    }

    private void setupAskCommunityButton(View root) {
        MaterialButton askButton = root.findViewById(R.id.act_com_button_ask);
        askButton.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AskCommunityActivity.class));
        });
    }

    private void setupRecyclerView(View root) {
        recyclerViewPosts = root.findViewById(R.id.act_com_recycler_posts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<CommunityPost> posts = CommunityDatabase.getAllPosts();
        postAdapter = new CommunityPostAdapter(requireContext(), posts, this);
        recyclerViewPosts.setAdapter(postAdapter);
    }

    private void setupFilterChips(View root) {
        filterChipGroup = root.findViewById(R.id.act_com_filter_chips);
        // Ensure an "All" chip at index 0
        if (filterChipGroup.getChildCount() == 0 || !(filterChipGroup.getChildAt(0) instanceof Chip)) {
            Chip allChip = new Chip(requireContext());
            allChip.setText("All");
            allChip.setChecked(true);
            allChip.setOnClickListener(v -> { currentFilter = "All"; loadPosts(); });
            filterChipGroup.addView(allChip, 0);
        }
        for (int i = 1; i < filterChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) filterChipGroup.getChildAt(i);
            chip.setOnClickListener(v -> {
                currentFilter = chip.getText().toString();
                loadPosts();
            });
        }
    }

    private void setupSearch(View root) {
        // placeholder for future search hookup
        searchEditText = null;
    }

    private void loadPosts() {
        List<CommunityPost> posts;
        if ("All".equals(currentFilter)) {
            posts = CommunityDatabase.getAllPosts();
        } else {
            posts = CommunityDatabase.getPostsByCropType(currentFilter);
        }
        if (postAdapter != null) {
            postAdapter.updatePosts(posts);
        }
    }

    @Override
    public void onLikeClick(CommunityPost post) {
        CommunityDatabase.likePost(post.getId());
        loadPosts();
    }

    @Override
    public void onDislikeClick(CommunityPost post) {
        CommunityDatabase.dislikePost(post.getId());
        loadPosts();
    }

    @Override
    public void onCommentClick(CommunityPost post) {
        CommunityDatabase.addComment(post.getId());
        loadPosts();
    }

    @Override
    public void onShareClick(CommunityPost post) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, post.getTitle() + "\n\n" + post.getDescription());
        startActivity(Intent.createChooser(shareIntent, "Share this post"));
    }
}
