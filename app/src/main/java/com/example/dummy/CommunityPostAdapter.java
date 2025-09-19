package com.example.dummy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommunityPostAdapter extends RecyclerView.Adapter<CommunityPostAdapter.PostViewHolder> {

    private List<CommunityPost> postsList;
    private CommunityActivity activity;

    public CommunityPostAdapter(List<CommunityPost> postsList, CommunityActivity activity) {
        this.postsList = postsList;
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
        CommunityPost post = postsList.get(position);
        
        holder.userName.setText(post.getUserName());
        holder.userLocation.setText(post.getUserLocation());
        holder.timeAgo.setText(post.getTimeAgo());
        holder.cropType.setText(post.getCropType());
        holder.title.setText(post.getTitle());
        holder.description.setText(post.getDescription());
        holder.likesCount.setText(String.valueOf(post.getLikesCount()));
        holder.dislikesCount.setText(String.valueOf(post.getDislikesCount()));
        holder.answersCount.setText(post.getAnswersCount() + " answers");
        
        // Set crop icon based on crop type
        switch (post.getCropType().toLowerCase()) {
            case "tomato":
                holder.cropIcon.setImageResource(R.drawable.ic_tomato);
                break;
            case "pea":
                holder.cropIcon.setImageResource(R.drawable.ic_pea);
                break;
            case "cotton":
                holder.cropIcon.setImageResource(R.drawable.ic_cotton);
                break;
            case "onion":
                holder.cropIcon.setImageResource(R.drawable.ic_onion);
                break;
            default:
                holder.cropIcon.setImageResource(R.drawable.ic_tomato);
                break;
        }

        // Set click listeners
        holder.likeIcon.setOnClickListener(v -> {
            int currentLikes = post.getLikesCount();
            post.setLikesCount(currentLikes + 1);
            holder.likesCount.setText(String.valueOf(post.getLikesCount()));
        });

        holder.dislikeIcon.setOnClickListener(v -> {
            int currentDislikes = post.getDislikesCount();
            post.setDislikesCount(currentDislikes + 1);
            holder.dislikesCount.setText(String.valueOf(post.getDislikesCount()));
        });

        holder.whatsappIcon.setOnClickListener(v -> {
            // Handle WhatsApp share
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage, userAvatar, cropIcon, likeIcon, dislikeIcon, whatsappIcon;
        TextView userName, userLocation, timeAgo, cropType, title, description;
        TextView likesCount, dislikesCount, answersCount;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            
            postImage = itemView.findViewById(R.id.item_post_image);
            userAvatar = itemView.findViewById(R.id.item_user_avatar);
            cropIcon = itemView.findViewById(R.id.item_crop_icon);
            likeIcon = itemView.findViewById(R.id.item_like_icon);
            dislikeIcon = itemView.findViewById(R.id.item_dislike_icon);
            whatsappIcon = itemView.findViewById(R.id.item_whatsapp_icon);
            
            userName = itemView.findViewById(R.id.item_user_name);
            userLocation = itemView.findViewById(R.id.item_user_location);
            timeAgo = itemView.findViewById(R.id.item_time_ago);
            cropType = itemView.findViewById(R.id.item_crop_type);
            title = itemView.findViewById(R.id.item_post_title);
            description = itemView.findViewById(R.id.item_post_description);
            likesCount = itemView.findViewById(R.id.item_likes_count);
            dislikesCount = itemView.findViewById(R.id.item_dislikes_count);
            answersCount = itemView.findViewById(R.id.item_answers_count);
        }
    }
}
