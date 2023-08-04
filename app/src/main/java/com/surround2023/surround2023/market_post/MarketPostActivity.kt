package com.surround2023.surround2023.market_post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.surround2023.surround2023.R

class MarketPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_post)

        val btnLike: ImageButton = findViewById(R.id.btnLike)
        val btnJoin: Button = findViewById(R.id.btnJoin)

        btnJoin.setOnClickListener { // 공구 참여 버튼 클릭
            // 눌러짐 색상 변경
            btnJoin.backgroundTintList = getColorStateList(R.color.subDeepGreen)

            // 250ms(0.25초) 후에 원래 색상으로 복원
            btnJoin.postDelayed({
                btnJoin.backgroundTintList = getColorStateList(R.color.themeGreen)
            }, 250)
            // BottomSheet 보여주기
            val joinBottomSheet = JoinMarketBottomSheetFragment()
            joinBottomSheet.show(supportFragmentManager, joinBottomSheet.tag)
        }

        btnLike.setOnClickListener{ // 좋아요 버튼 클릭
            btnLike.isSelected = !btnLike.isSelected // 좋아요 누른 상태 -> 채워진 하트
        }
    }
}