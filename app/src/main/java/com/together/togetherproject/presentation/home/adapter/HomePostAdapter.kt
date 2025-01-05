package com.together.togetherproject.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.together.togetherproject.R
import com.together.togetherproject.data.model.Post

class HomePostAdapter(private var posts: List<Post>) :
    RecyclerView.Adapter<HomePostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tvPostTitle)
        private val contentTextView: TextView = itemView.findViewById(R.id.tvPostContent)
        private val startAddressTextView: TextView = itemView.findViewById(R.id.tvStartAddress)
        private val destinationTextView: TextView = itemView.findViewById(R.id.tvDestination)

        fun bind(post: Post) {
            titleTextView.text = post.title
            contentTextView.text = post.content
            startAddressTextView.text =post.startAddress
            destinationTextView.text = post.destination
        }
    }
}