package com.example.parstagram

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.ParseException
import java.util.*
private val TAG = "PostAdapter"
private val SECOND_MILLIS = 1000;
private val MINUTE_MILLIS = 60 * SECOND_MILLIS
private val HOUR_MILLIS = 60 * MINUTE_MILLIS
private val DAY_MILLIS = 24 * HOUR_MILLIS
class PostAdapter(val context: Context, val posts: List<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder{
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent,false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    @RequiresApi(Build.VERSION_CODES.N)
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvUsername: TextView
        val ivImage: ImageView
        val tvDescription: TextView
        val tvTimestamp: TextView
        val ivProfilePicture: ImageView

        init {
            tvUsername = itemView.findViewById(R.id.tvUsername)
            ivImage = itemView.findViewById(R.id.ivImage)
            tvDescription = itemView.findViewById(R.id.tvDescription)
            tvTimestamp = itemView.findViewById(R.id.tvTime)
            ivProfilePicture = itemView.findViewById(R.id.ivProfilePicture)
        }

        fun bind(post: Post){
            tvDescription.text = post.getDescription()
            tvUsername.text = post.getUser()?.username
            tvTimestamp.text = post.createdAt.toString()
            Glide.with(itemView.context)
                .load(post.getImage()?.url)
                .into(ivImage)
            Glide.with(itemView.context)
                .load(post.getUser()?.getParseFile("profilePicture")?.url)
                .into(ivProfilePicture)
        }
        @RequiresApi(Build.VERSION_CODES.N)
        fun getRelativeTimeAgo(date: Date) : String{
            try {
                val time = date.time
                val now = System.currentTimeMillis()

                val diff = now - time
                if(diff < MINUTE_MILLIS) return "just now"
                else if(diff < 2 * MINUTE_MILLIS) return "a minute ago"
                else if(diff < 60 * MINUTE_MILLIS){
                    val mins = diff / MINUTE_MILLIS
                    return "$mins m"
                } else if(diff < 120 * MINUTE_MILLIS) return "an hour ago"
                else if(diff < 24 * HOUR_MILLIS){
                    val hours = diff / HOUR_MILLIS
                    return "$hours h"
                } else if(diff < 48 * HOUR_MILLIS) return "yesterday"
                else {
                    val days = diff / DAY_MILLIS
                    return "$days d"
                }
            } catch (e: ParseException) {
                Log.e(TAG, "Relative time Error $e")
            }
            return ""
        }

    }
}