//package com.surround2023.surround2023.market
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.surround2023.surround2023.R
//import com.surround2023.surround2023.community.Communitymemo
//
//class Marketadapter : RecyclerView.Adapter<Marketadapter.PostViewHolder>() {
//
//    private var postList: List<Communitymemo> = emptyList()
//
//    fun setData(posts: List<Communitymemo>) {
//        postList = posts
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_communityitem, parent, false)
//        return PostViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
//        holder.bind(postList[position])
//    }
//
//    override fun getItemCount(): Int = postList.size
//
//    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val ivImage: ImageView = itemView.findViewById(R.id.strawberry)
//        private val tvCategory: TextView = itemView.findViewById(R.id.category)
//        private val tvTitle: TextView = itemView.findViewById(R.id.title)
//        private val tvDetail: TextView = itemView.findViewById(R.id.detail)
//        private val tvComment: TextView = itemView.findViewById(R.id.comment)
//
//        fun bind(post: Communitymemo) {
//            ivImage.setImageResource(post.image)
//            tvCategory.text = post.category
//            tvTitle.text = post.postTitle
//            tvDetail.text = post.postContent
//            tvComment.text = post.comment
//        }
//    }
//}
