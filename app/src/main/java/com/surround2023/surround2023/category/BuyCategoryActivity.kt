package com.surround2023.surround2023.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.surround2023.surround2023.databinding.ActivityBuyCategoryBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class BuyCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyCategoryBinding
    val db = FirebaseFirestore.getInstance()
    val postsCollection = db.collection("Market")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.latestBtn.setOnClickListener {

        }

        binding.foodBtn.setOnClickListener {
            onCategoryButtonClick("Food")
        }

        binding.onlyWomanBtn.setOnClickListener {
            onCategoryButtonClick("isWomanOnly")
        }

        binding.onlyManBtn.setOnClickListener {
            onCategoryButtonClick("isManOnly")
        }

        binding.clothingBtn.setOnClickListener {
            onCategoryButtonClick("Clothing")
        }

        binding.hobbyBtn.setOnClickListener {
            onCategoryButtonClick("Hobby")
        }

        binding.petBtn.setOnClickListener {
            onCategoryButtonClick("Pet")
        }

        binding.interiorBtn.setOnClickListener {
            onCategoryButtonClick("Interior")
        }
    }

    fun onCategoryButtonClick(category:String) {
        postsCollection.whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    // 각 게시글의 데이터 출력 (원하는 동작 수행)
                    val title = document.getString("postTitle")
                    val content = document.getString("postContent")
                    Log.d("Post","Title: $title, Content: $content")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Post","Error getting documents: $exception")
            }
    }

    fun latestButtonClick(){
        postsCollection.orderBy("postDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val title = document.getString("title")
                    val content = document.getString("content")
                    val timestamp = document.getTimestamp("timestamp")
                    if (title != null && content != null && timestamp != null) {
//                        val post = Post(title, content, timestamp)
//                        posts.add(post)
                    }
                }
//                postAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }
}