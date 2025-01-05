package com.together.togetherproject.presentation.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.together.togetherproject.R
import com.together.togetherproject.data.model.Comment
import com.together.togetherproject.databinding.ItemCommentBinding

class CommentAdapter(
    private val onChatClick: (Comment) -> Unit,
    private val onDeleteClick: (Comment) -> Unit
) : ListAdapter<Comment, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            binding.comment = comment
            binding.executePendingBindings()
            binding.tvCommentAuthor.text = comment.authorNickname

            binding.ivCommentOptions.setOnClickListener {
                val popupMenu = PopupMenu(binding.root.context, it)
                popupMenu.inflate(R.menu.comment_options_menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_chat -> {
                            onChatClick(comment)
                            true
                        }
                        R.id.action_delete -> {
                            onDeleteClick(comment)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }
        }
    }

    class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }
}