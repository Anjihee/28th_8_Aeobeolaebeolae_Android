package com.surround2023.surround2023.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.surround2023.surround2023.R
import com.surround2023.surround2023.databinding.ActivityBuyCategoryBinding

class BuyCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.updateBtn.setOnClickListener {

        }

        binding.foodBtn.setOnClickListener {

        }

        binding.onlyWomanBtn.setOnClickListener {

        }

        binding.onlyManBtn.setOnClickListener {

        }

        binding.clothingBtn.setOnClickListener {

        }

        binding.hobbyBtn.setOnClickListener {

        }

        binding.petBtn.setOnClickListener {

        }

        binding.interiorBtn.setOnClickListener {

        }
    }
}