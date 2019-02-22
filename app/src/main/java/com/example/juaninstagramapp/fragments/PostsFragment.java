package com.example.juaninstagramapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.juaninstagramapp.Post;
import com.example.juaninstagramapp.PostsAdapter;
import com.example.juaninstagramapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {
    public static final String TAG = PostsFragment.class.getName();
    public static final int POST_QUERY_LIMIT = 20;

    protected PostsAdapter adapter;
    protected List<Post> posts;
    protected SwipeRefreshLayout swipeContainer;

    private RecyclerView rvPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        swipeContainer = view.findViewById(R.id.swipeContainer);

        rvPosts = view.findViewById(R.id.rvPosts);
        // create the data source and adapter
        posts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), posts);

        // set the adapter and layout manager on RV
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "Refreshing data");
                queryPost();
            }
        });

        swipeContainer.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        queryPost();
    }

    protected void queryPost() {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(POST_QUERY_LIMIT);

        // TODO - change to allow user to specify order
        postQuery.addAscendingOrder(Post.KEY_CREATED_AT);

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> mPosts, ParseException e) {
                if ( e != null ) {
                    Log.e(TAG, "Error querying post: ", e);
                    swipeContainer.setRefreshing(false);
                    return;
                }

                posts.addAll(mPosts);
                adapter.notifyDataSetChanged();

                // TODO - for debugging  / remove
                for (Post post : posts) {
                    Log.d(TAG, "Post: " + post.getDescription() + ". User: " + post.getUser().getUsername());
                }

                Log.d(TAG, "Success querying posts");
                swipeContainer.setRefreshing(false);
            }
        });
    }

}
