package com.surround2023.surround2023.category

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.surround2023.surround2023.community.CommunityholderActivity
import com.surround2023.surround2023.databinding.ActivityCommunityCategoryBinding
import com.surround2023.surround2023.market.MarketholderActivity

class CommunityCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunityCategoryBinding
    val db = FirebaseFirestore.getInstance()
    val postsCollection = db.collection("Community")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommunityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, CommunityholderActivity::class.java)


        binding.latestBtn.setOnClickListener {
            startActivity(intent)
        }

        binding.popularBtn.setOnClickListener {
            setResultAndFinish("인기순")
        }

        binding.freeBtn.setOnClickListener {
            setResultAndFinish("자유")
        }

        binding.AdBtn.setOnClickListener {
            setResultAndFinish("광고")
        }

        binding.loveBtn.setOnClickListener {
            setResultAndFinish("연애")
        }

        binding.workBtn.setOnClickListener {
            setResultAndFinish("진로")
        }

        binding.petBtn.setOnClickListener {
            setResultAndFinish("반려동물")
        }

        binding.neighborBtn.setOnClickListener {
            setResultAndFinish("동네소식")
        }
    }
    private fun setResultAndFinish(category: String) {
        val intent = Intent().apply {
            putExtra("category", category)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}