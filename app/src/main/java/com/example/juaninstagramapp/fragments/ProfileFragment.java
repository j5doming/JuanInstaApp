package com.example.juaninstagramapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.example.juaninstagramapp.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {
    public final static String TAG = ProfileFragment.class.getName();

    @Override
    protected void queryPost() {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(POST_QUERY_LIMIT);
        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());

        // TODO - change to allow user to specify order
        postQuery.addAscendingOrder(Post.KEY_CREATED_AT);




        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> mPosts, ParseException e) {
                if ( e != null ) {
                    Log.e(TAG, "Error querying user posts: ", e);
                    swipeContainer.setRefreshing(false);
                    return;
                }

                posts.addAll(mPosts);
                adapter.notifyDataSetChanged();

                // TODO - for debugging  / remove
                for (Post post : posts) {
                    Log.d(TAG, "Post: " + post.getDescription() + ". User: " + post.getUser().getUsername());
                }

                Log.d(TAG, "Success querying user posts");
                swipeContainer.setRefreshing(false);
            }
        });
    }
}
