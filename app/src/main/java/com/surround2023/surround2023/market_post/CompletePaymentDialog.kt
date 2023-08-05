package com.surround2023.surround2023.market_post

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.surround2023.surround2023.R

class CompletePaymentDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_complete_payment) // 다이얼로그 레이아웃 설정
        // 여기에 다이얼로그 내용을 초기화하고 이벤트를 설정할 수 있습니다.
        setCancelable(false) // 다른 화면 터치나 뒤로가기 버튼을 통해 창 닫히지 않게 설정

        val btnPopupClose = findViewById<TextView>(R.id.btnPopupClose)
        btnPopupClose.setOnClickListener{ // 확인 버튼 누르면 글 화면으로 돌아가기
            val intent = Intent(context, MarketPostActivity::class.java)
            context.startActivity(intent)
            dismiss()
        }
    }
}