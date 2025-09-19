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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    
    private List<Comment> commentList;
    
    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }
    
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.bind(comment);
    }
    
    @Override
    public int getItemCount() {
        return commentList.size();
    }
    
    class CommentViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView authorAvatar;
        private TextView authorName;
        private TextView authorLocation;
        private TextView commentTime;
        private TextView commentContent;
        private LinearLayout likeButton;
        private ImageView likeIcon;
        private TextView likesCount;
        
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            
            authorAvatar = itemView.findViewById(R.id.author_avatar);
            authorName = itemView.findViewById(R.id.author_name);
            authorLocation = itemView.findViewById(R.id.author_location);
            commentTime = itemView.findViewById(R.id.comment_time);
            commentContent = itemView.findViewById(R.id.comment_content);
            likeButton = itemView.findViewById(R.id.like_button);
            likeIcon = itemView.findViewById(R.id.like_icon);
            likesCount = itemView.findViewById(R.id.likes_count);
        }
        
        public void bind(Comment comment) {
            authorName.setText(comment.getAuthorName());
            authorLocation.setText(comment.getAuthorLocation());
            commentTime.setText(comment.getTimeAgo());
            commentContent.setText(comment.getContent());
            likesCount.setText(String.valueOf(comment.getLikesCount()));
            
            // Set like state
            updateLikeState(comment);
            
            // Set click listeners
            likeButton.setOnClickListener(v -> {
                if (comment.isLiked()) {
                    comment.setLiked(false);
                    comment.setLikesCount(comment.getLikesCount() - 1);
                } else {
                    comment.setLiked(true);
                    comment.setLikesCount(comment.getLikesCount() + 1);
                }
                updateLikeState(comment);
                notifyItemChanged(getAdapterPosition());
            });
        }
        
        private void updateLikeState(Comment comment) {
            if (comment.isLiked()) {
                likeIcon.setImageResource(R.drawable.ic_thumb_up_filled);
                likeIcon.setColorFilter(itemView.getContext().getColor(R.color.primary_blue));
            } else {
                likeIcon.setImageResource(R.drawable.ic_thumb_up_outline);
                likeIcon.setColorFilter(itemView.getContext().getColor(R.color.text_secondary));
            }
        }
    }
}
