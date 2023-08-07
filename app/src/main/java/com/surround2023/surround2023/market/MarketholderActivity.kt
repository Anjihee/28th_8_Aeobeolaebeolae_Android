package com.surround2023.surround2023.market

import androidx.appcompat.app.AppCompatActivity

/* import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.surround2023.surround2023.databinding.ActivityMarketholderBinding
import com.surround2023.surround2023.market.Marketmemo

class MarketholderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarketholderBinding
    private lateinit var adapter: Marketadapter
    private val db = FirebaseFirestore.getInstance()
    private val marketRef = db.collection("Market").document("BzHVyJjtuIW5CDDhMnQ7")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketholderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 초기화
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = Marketadapter()
        binding.recyclerview.adapter = adapter

        // Firestore에서 데이터 가져오기
        fetchMarketDataFromFirestore()
    }

    private fun fetchMarketDataFromFirestore() {
        marketRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val image = documentSnapshot.getLong("image")?.toInt() ?: 0
                    val postTitle = documentSnapshot.getString("postTitle") ?: ""
                    val postContent = documentSnapshot.getString("postContent") ?: ""

                    val market = Marketmemo(image, postTitle, postContent)
                    adapter.setData(listOf(market))
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }
} */