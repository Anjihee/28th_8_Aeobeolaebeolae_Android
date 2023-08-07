package com.surround2023.surround2023.community

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.surround2023.surround2023.R
import com.surround2023.surround2023.community.Communityadapter
import com.surround2023.surround2023.community.Communitymemo

class CommunityholderActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Communityadapter
    private val db = FirebaseFirestore.getInstance()
    private val postsCollectionRef = db.collection("posts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_communityholder)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = Communityadapter()
        recyclerView.adapter = adapter

        // Firestore에서 데이터 가져오기
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
                // 에러 처리
                Toast.makeText(this, "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }


}
