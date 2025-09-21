package com.example.dummy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CommunityPostAdapter extends RecyclerView.Adapter<CommunityPostAdapter.PostViewHolder> {

    private List<CommunityPost> posts;
    private Context context;
    private OnPostInteractionListener listener;

    public interface OnPostInteractionListener {
        void onLikeClick(CommunityPost post);
        void onDislikeClick(CommunityPost post);
        void onCommentClick(CommunityPost post);
        void onShareClick(CommunityPost post);
    }

    public CommunityPostAdapter(Context context, List<CommunityPost> posts, OnPostInteractionListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_community_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        CommunityPost post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updatePosts(List<CommunityPost> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private ImageView postImage;
        private TextView authorName;
        private TextView authorLocation;
        private TextView postTime;
        private TextView cropType;
        private TextView postTitle;
        private TextView postDescription;
        private MaterialButton likeButton;
        private MaterialButton dislikeButton;
        private MaterialButton commentButton;
        private MaterialButton shareButton;
        private TextView likeCount;
        private TextView dislikeCount;
        private TextView commentCount;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.item_post_image);
            authorName = itemView.findViewById(R.id.item_post_author_name);
            authorLocation = itemView.findViewById(R.id.item_post_author_location);
            postTime = itemView.findViewById(R.id.item_post_time);
            cropType = itemView.findViewById(R.id.item_post_crop_type);
            postTitle = itemView.findViewById(R.id.item_post_title);
            postDescription = itemView.findViewById(R.id.item_post_description);
            likeButton = itemView.findViewById(R.id.item_post_like_button);
            dislikeButton = itemView.findViewById(R.id.item_post_dislike_button);
            commentButton = itemView.findViewById(R.id.item_post_comment_button);
            shareButton = itemView.findViewById(R.id.item_post_share_button);
            likeCount = itemView.findViewById(R.id.item_post_like_count);
            dislikeCount = itemView.findViewById(R.id.item_post_dislike_count);
            commentCount = itemView.findViewById(R.id.item_post_comment_count);
        }

        public void bind(CommunityPost post) {
            // Set post data
            authorName.setText(post.getAuthorName());
            authorLocation.setText(post.getAuthorLocation());
            postTitle.setText(post.getTitle());
            postDescription.setText(post.getDescription());
            
            // Set crop type
            if (post.getCropType() != null && !post.getCropType().isEmpty()) {
                cropType.setText(post.getCropType());
                cropType.setVisibility(View.VISIBLE);
            } else {
                cropType.setVisibility(View.GONE);
            }
            
            // Set time
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
            postTime.setText(sdf.format(post.getTimestamp()));
            
            // Set interaction counts
            likeCount.setText(String.valueOf(post.getLikes()));
            dislikeCount.setText(String.valueOf(post.getDislikes()));
            commentCount.setText(String.valueOf(post.getComments()));
            
            // Set post image
            if (post.getImageUri() != null && !post.getImageUri().isEmpty()) {
                try {
                    Uri imageUri = Uri.parse(post.getImageUri());
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                    postImage.setImageBitmap(bitmap);
                    postImage.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                    postImage.setVisibility(View.GONE);
                }
            } else {
                postImage.setVisibility(View.GONE);
            }
            
            // Set click listeners
            likeButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLikeClick(post);
                }
            });
            
            dislikeButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDislikeClick(post);
                }
            });
            
            commentButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCommentClick(post);
                }
            });
            
            shareButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onShareClick(post);
                }
            });
        }
    }
}
