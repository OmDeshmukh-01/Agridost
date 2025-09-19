package com.example.dummy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    
    private List<Post> postList;
    private CommunityActivity activity;
    
    public PostAdapter(List<Post> postList, CommunityActivity activity) {
        this.postList = postList;
        this.activity = activity;
    }
    
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_community_post, parent, false);
        return new PostViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.bind(post);
    }
    
    @Override
    public int getItemCount() {
        return postList.size();
    }
    
    public void updatePosts(List<Post> newPosts) {
        this.postList = newPosts;
        notifyDataSetChanged();
    }
    
    class PostViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView postImage;
        private TextView authorName;
        private TextView authorLocation;
        private TextView postTime;
        private ImageView cropIcon;
        private TextView cropType;
        private TextView postTitle;
        private TextView postContent;
        private TextView translateLink;
        private TextView commentsCount;
        private LinearLayout likeButton;
        private ImageView likeIcon;
        private TextView likesCount;
        private LinearLayout dislikeButton;
        private ImageView dislikeIcon;
        private TextView dislikesCount;
        private LinearLayout commentButton;
        private LinearLayout shareButton;
        
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            
            postImage = itemView.findViewById(R.id.post_image);
            authorName = itemView.findViewById(R.id.author_name);
            authorLocation = itemView.findViewById(R.id.author_location);
            postTime = itemView.findViewById(R.id.post_time);
            cropIcon = itemView.findViewById(R.id.crop_icon);
            cropType = itemView.findViewById(R.id.crop_type);
            postTitle = itemView.findViewById(R.id.post_title);
            postContent = itemView.findViewById(R.id.post_content);
            translateLink = itemView.findViewById(R.id.translate_link);
            commentsCount = itemView.findViewById(R.id.comments_count);
            likeButton = itemView.findViewById(R.id.like_button);
            likeIcon = itemView.findViewById(R.id.like_icon);
            likesCount = itemView.findViewById(R.id.likes_count);
            dislikeButton = itemView.findViewById(R.id.dislike_button);
            dislikeIcon = itemView.findViewById(R.id.dislike_icon);
            dislikesCount = itemView.findViewById(R.id.dislikes_count);
            commentButton = itemView.findViewById(R.id.comment_button);
            shareButton = itemView.findViewById(R.id.share_button);
        }
        
        public void bind(Post post) {
            // Set post content
            authorName.setText(post.getAuthorName());
            authorLocation.setText(post.getAuthorLocation());
            postTime.setText(post.getTimeAgo());
            cropType.setText(post.getCropType());
            postTitle.setText(post.getTitle());
            postContent.setText(post.getContent());
            commentsCount.setText(post.getCommentsCount() + " answers");
            likesCount.setText(String.valueOf(post.getLikesCount()));
            dislikesCount.setText(String.valueOf(post.getDislikesCount()));
            
            // Set crop icon based on crop type
            setCropIcon(post.getCropType());
            
            // Load post image - for now just use placeholder
            // In a real app, you would implement proper image loading here
            postImage.setImageResource(R.drawable.ic_placeholder_image);
            
            // Set like/dislike states
            updateLikeDislikeStates(post);
            
            // Set click listeners
            likeButton.setOnClickListener(v -> activity.onPostLike(post));
            dislikeButton.setOnClickListener(v -> activity.onPostDislike(post));
            commentButton.setOnClickListener(v -> activity.onPostComment(post));
            shareButton.setOnClickListener(v -> activity.onPostShare(post));
            
            // Translate link click
            translateLink.setOnClickListener(v -> {
                // Handle translate functionality
                // This could open a translation dialog or translate the text inline
            });
        }
        
        private void setCropIcon(String cropType) {
            int iconRes = R.drawable.ic_tomato; // Default
            
            switch (cropType.toLowerCase()) {
                case "tomato":
                    iconRes = R.drawable.ic_tomato;
                    break;
                case "pea":
                    iconRes = R.drawable.ic_pea;
                    break;
                case "cotton":
                    iconRes = R.drawable.ic_cotton;
                    break;
                case "onion":
                    iconRes = R.drawable.ic_onion;
                    break;
                case "wheat":
                    iconRes = R.drawable.ic_wheat;
                    break;
                case "rice":
                    iconRes = R.drawable.ic_rice;
                    break;
                case "potato":
                    iconRes = R.drawable.ic_potato;
                    break;
                case "corn":
                    iconRes = R.drawable.ic_corn;
                    break;
                case "soybean":
                    iconRes = R.drawable.ic_soybean;
                    break;
                case "sugarcane":
                    iconRes = R.drawable.ic_sugarcane;
                    break;
            }
            
            cropIcon.setImageResource(iconRes);
        }
        
        private void updateLikeDislikeStates(Post post) {
            if (post.isLiked()) {
                likeIcon.setImageResource(R.drawable.ic_thumb_up_filled);
                likeIcon.setColorFilter(itemView.getContext().getColor(R.color.primary_blue));
            } else {
                likeIcon.setImageResource(R.drawable.ic_thumb_up_outline);
                likeIcon.setColorFilter(itemView.getContext().getColor(R.color.text_secondary));
            }
            
            if (post.isDisliked()) {
                dislikeIcon.setImageResource(R.drawable.ic_thumb_down_filled);
                dislikeIcon.setColorFilter(itemView.getContext().getColor(R.color.primary_blue));
            } else {
                dislikeIcon.setImageResource(R.drawable.ic_thumb_down_outline);
                dislikeIcon.setColorFilter(itemView.getContext().getColor(R.color.text_secondary));
            }
        }
    }
}
