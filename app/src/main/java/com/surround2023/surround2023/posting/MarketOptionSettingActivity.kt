package com.surround2023.surround2023.posting

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.surround2023.surround2023.R

class MarketOptionSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_option_setting)

        // 소프트키(네비게이션 바), 상태바를 숨기기 위한 플래그 설정
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        val btnDoneSetting: Button = findViewById(R.id.doneSetting)
        btnDoneSetting.setOnClickListener {
            // 완료 버튼을 클릭하면 종료
            onBackPressed()
        }

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            // 뒤로가기 버튼을 클릭하면 종료
            onBackPressed()
        }
    }

    override fun onBackPressed() { // 뒤로가기
        super.onBackPressed()
        finish() // 현재 액티비티 종료
    }
}