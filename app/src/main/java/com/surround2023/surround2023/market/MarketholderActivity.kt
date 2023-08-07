package com.surround2023.surround2023.market

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.surround2023.surround2023.R
import com.surround2023.surround2023.community.Communitymemo

class MarketholderActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Marketadapter
    private val db = FirebaseFirestore.getInstance()
    private val postsCollectionRef = db.collection("posts")
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_communityholder)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Marketadapter()
        recyclerView.adapter = adapter

        fetchPostsFromFirestore()
    }

    private fun fetchPostsFromFirestore() {
        postsCollectionRef.orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val postList = mutableListOf<Communitymemo>()
                for (document in result) {
                    val image = document.getLong("image")?.toInt() ?: 0
                    val category = document.getString("category") ?: ""
                    val title = document.getString("title") ?: ""
                    val detail = document.getString("detail") ?: ""
                    val comment = document.getString("comment") ?: ""

                    val post = Communitymemo(image, category, title, detail, comment)
                    postList.add(post)
                }
                adapter.setData(postList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }
}