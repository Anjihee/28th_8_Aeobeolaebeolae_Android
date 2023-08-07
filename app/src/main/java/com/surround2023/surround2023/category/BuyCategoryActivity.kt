package com.surround2023.surround2023.category

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.surround2023.surround2023.databinding.ActivityBuyCategoryBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.surround2023.surround2023.market.MarketholderActivity

class BuyCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyCategoryBinding
    val db = FirebaseFirestore.getInstance()
    val postsCollection = db.collection("Market")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityBuyCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MarketholderActivity::class.java)

        binding.latestBtn.setOnClickListener {
            startActivity(intent)
        }

        binding.foodBtn.setOnClickListener {
            setResultAndFinish("음식")
        }

        binding.onlyWomanBtn.setOnClickListener {
            setResultAndFinish("isWomanOnly")
        }

        binding.onlyManBtn.setOnClickListener {
            setResultAndFinish("isManOnly")
        }

        binding.clothingBtn.setOnClickListener {
            setResultAndFinish("의류")
        }

        binding.hobbyBtn.setOnClickListener {
            setResultAndFinish("취미")
        }

        binding.petBtn.setOnClickListener {
            setResultAndFinish("반려동물")
        }

        binding.interiorBtn.setOnClickListener {
            setResultAndFinish("인테리어")
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