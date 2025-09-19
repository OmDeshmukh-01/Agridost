package com.example.dummy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class CommentDialog extends DialogFragment {
    
    private static final String ARG_POST = "post";
    
    private Post post;
    private RecyclerView recyclerViewComments;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private EditText editTextComment;
    private Button buttonPostComment;
    private TextView textViewNoComments;
    
    public interface OnCommentAddedListener {
        void onCommentAdded(Comment comment);
    }
    
    private OnCommentAddedListener listener;
    
    public static CommentDialog newInstance(Post post) {
        CommentDialog dialog = new CommentDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_POST, post);
        dialog.setArguments(args);
        return dialog;
    }
    
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCommentAddedListener) {
            listener = (OnCommentAddedListener) context;
        }
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post) getArguments().getSerializable(ARG_POST);
        }
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_comments, container, false);
        
        initializeViews(view);
        setupToolbar(view);
        setupRecyclerView();
        setupClickListeners();
        loadComments();
        
        return view;
    }
    
    private void initializeViews(View view) {
        recyclerViewComments = view.findViewById(R.id.recycler_view_comments);
        editTextComment = view.findViewById(R.id.edit_text_comment);
        buttonPostComment = view.findViewById(R.id.button_post_comment);
        textViewNoComments = view.findViewById(R.id.text_view_no_comments);
    }
    
    private void setupToolbar(View view) {
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        
        TextView textViewPostTitle = view.findViewById(R.id.text_view_post_title);
        textViewPostTitle.setText(post.getTitle());
    }
    
    private void setupRecyclerView() {
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewComments.setAdapter(commentAdapter);
    }
    
    private void setupClickListeners() {
        buttonPostComment.setOnClickListener(v -> {
            String commentText = editTextComment.getText().toString().trim();
            if (!commentText.isEmpty()) {
                addComment(commentText);
            }
        });
    }
    
    private void loadComments() {
        // Load existing comments for this post
        commentList.clear();
        commentList.addAll(post.getComments());
        commentAdapter.notifyDataSetChanged();
        
        updateNoCommentsVisibility();
    }
    
    private void addComment(String commentText) {
        Comment comment = new Comment();
        comment.setId(String.valueOf(System.currentTimeMillis()));
        comment.setPostId(post.getId());
        comment.setAuthorName("Current User"); // This should come from user session
        comment.setAuthorLocation("Your Location"); // This should come from user session
        comment.setContent(commentText);
        comment.setTimeAgo("now");
        comment.setLikesCount(0);
        comment.setLiked(false);
        
        commentList.add(comment);
        post.addComment(comment);
        commentAdapter.notifyItemInserted(commentList.size() - 1);
        
        editTextComment.setText("");
        updateNoCommentsVisibility();
        
        if (listener != null) {
            listener.onCommentAdded(comment);
        }
    }
    
    private void updateNoCommentsVisibility() {
        if (commentList.isEmpty()) {
            textViewNoComments.setVisibility(View.VISIBLE);
            recyclerViewComments.setVisibility(View.GONE);
        } else {
            textViewNoComments.setVisibility(View.GONE);
            recyclerViewComments.setVisibility(View.VISIBLE);
        }
    }
}
