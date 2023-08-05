package com.surround2023.surround2023.market_post

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.surround2023.surround2023.R

class JoinMarketActivity : AppCompatActivity() {

    private lateinit var radioButtons: Array<RadioButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_market)
        
        val btnBack : ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener { // '뒤로가기' 버튼 클릭 시
            // '뒤로가기' 버튼을 클릭하면 공구 글 페이지를 열도록 인텐트 생성
            val intent = Intent(this@JoinMarketActivity, MarketPostActivity::class.java)
            startActivity(intent)
        }

        radioButtons = arrayOf( // RadioGroup 대신 GridLayout을 사용했으므로 임의의 배열을 선언해 묶어줌
            findViewById<RadioButton>(R.id.payCard),
            findViewById<RadioButton>(R.id.payNaver),
            findViewById<RadioButton>(R.id.payKakao),
            findViewById<RadioButton>(R.id.payPhone),
            findViewById<RadioButton>(R.id.payDeposit)
        )

        for (radioButton in radioButtons) {
            radioButton.setOnClickListener {
                onRadioButtonClicked(radioButton)
            }
        }

        val payingBtn : Button = findViewById(R.id.payingBtn)
        payingBtn.setOnClickListener {
            val completeDialog = CompletePaymentDialog(this) // 결제 완료 창 Dialog 띄우기
            completeDialog.show()
        }
    }
    fun onRadioButtonClicked(selectedButton: RadioButton) { // 한 번에 하나의 라디오버튼만 선택될 수 있도록 함수 선언
        // 모든 버튼을 선택 해제
        for (radioButton in radioButtons) {
            radioButton.isChecked = false
        }

        // 선택된 버튼만 선택
        selectedButton.isChecked = true

        // 선택된 버튼에 대한 작업 수행
        when (selectedButton.id) {
            R.id.payCard -> {
                // '신용/체크카드 결제' 선택 시 처리
            }

            R.id.payNaver -> {
                // '네이버페이 결제' 선택 시 처리
            }

            R.id.payKakao -> {
                // '카카오페이 결제' 선택 시 처리
            }

            R.id.payPhone -> {
                // '휴대폰 결제' 선택 시 처리
            }

            R.id.payDeposit -> {
                // '무통장입금 결제' 선택 시 처리
            }
        }
    }
}