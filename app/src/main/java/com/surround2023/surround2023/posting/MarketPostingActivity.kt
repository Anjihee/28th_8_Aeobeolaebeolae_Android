package com.surround2023.surround2023.posting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.surround2023.surround2023.R

class MarketPostingActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1 // 이미지 선택 요청 코드

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_posting)

        val btnSettingOption: Button = findViewById(R.id.optionSettingBtn)
        btnSettingOption.setOnClickListener {
            // 버튼을 클릭하면 다른 액티비티를 열도록 인텐트 생성
            val intent = Intent(this@MarketPostingActivity, MarketOptionSettingActivity::class.java)
            startActivity(intent)
        }
    }
}
