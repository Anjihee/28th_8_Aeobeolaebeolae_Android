package com.surround2023.surround2023.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.surround2023.surround2023.R
import com.surround2023.surround2023.community.Communitymemo

class Communityadapter : RecyclerView.Adapter<Communityadapter.PostViewHolder>() {

    private var postList: List<Communitymemo> = emptyList()

    fun setData(posts: List<Communitymemo>) {
        postList = posts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_communityitem, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val binding = ItemCommunitymemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int = postList.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder() {
        fun bind(post: Communitymemo) {
            binding.ivImage.setImageResource(post.image)
            binding.tvCategory.text = post.category
            binding.tvTitle.text = post.title
            binding.tvDetail.text = post.detail
            binding.tvComment.text = post.comment
        }
    }
}