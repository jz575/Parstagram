package com.example.parstagram.fragments

import android.util.Log
import com.example.parstagram.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

private val TAG = "PersonalFeedFragment"
class ProfileFragment : HomeFragment() {

    override fun queryPosts(){
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //find all Post objects in server
        query.include(Post.KEY_USER)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        query.limit = 20
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if(e != null) {
                    Log.e(TAG, "Error fetching posts")
                } else {
                    if(posts != null) {
                        allPosts.clear()
                        for(post in posts) {
                            Log.i(TAG,"Post:" + post.getDescription())
                        }
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                        swipeContainer.isRefreshing = false
                    }
                }
            }
        })
    }
}