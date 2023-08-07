package com.surround2023.surround2023.community

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User
import com.surround2023.surround2023.R

class Communityadapter (
    private val mContext : Context,
    private val mList : MutableList<Communitymemo>
): RecyclerView.Adapter<Communityadapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val userImage: ImageView = itemView.findViewById(R.id.strawberry)
        private val userNameText : TextView = itemView.findViewById(R.id.title)

        fun bind (communitymemo: Communitymemo) {
            userNameText.text = communitymemo.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.activity_communityitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mList[position]
        holder.bind(user)
    }

    override fun getItemCount() = mList.size

}

