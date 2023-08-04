package com.surround2023.surround2023.posting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.surround2023.surround2023.R

class MarketOptionSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_option_setting)

        val btnDoneSetting: Button = findViewById(R.id.doneSetting)
        btnDoneSetting.setOnClickListener {
            // 완료 버튼을 클릭하면 다른 액티비티를 열도록 인텐트 생성
            val intent = Intent(this@MarketOptionSettingActivity, MarketPostingActivity::class.java)
            startActivity(intent)
        }

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            // 뒤로가기 버튼을 클릭하면 다른 액티비티를 열도록 인텐트 생성
            val intent = Intent(this@MarketOptionSettingActivity, MarketPostingActivity::class.java)
            startActivity(intent)
        }
    }
}