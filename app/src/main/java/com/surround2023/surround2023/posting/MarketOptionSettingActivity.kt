package com.surround2023.surround2023.posting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.surround2023.surround2023.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

        val editDeadLine : EditText = findViewById(R.id.editDeadLine)
        val btnSettingDeadline : Button = findViewById(R.id.btnSettingDeadline)

        var deadlineD = ""
        var deadlineData: Date? = null
        var deadlineTimestamp = Timestamp.now()

        btnSettingDeadline.setOnClickListener {
            deadlineD = editDeadLine.text.toString()

            val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            deadlineData = try {
                dateFormat.parse(deadlineD)
            } catch (e: Exception) {
                null
            }

            if (deadlineData != null) {
                deadlineTimestamp = Timestamp(deadlineData!!)
            }
        }

        val sexOptionGroup : RadioGroup = findViewById(R.id.sexOptionGroup)
        var sexOption = "거래 불가"

        sexOptionGroup.setOnCheckedChangeListener { radioGroup, checkedId -> // 라디오 버튼 선택시
            val selectedSexOption: RadioButton = findViewById(checkedId)

            when (selectedSexOption.id) {
                R.id.noneSexOption -> sexOption = "모두 거래 가능"
                R.id.femaleSexOption -> sexOption = "여성만 거래"
                R.id.maleSexOption -> sexOption = "남성만 거래"
            }
        }

        val btnDoneSetting: Button = findViewById(R.id.doneSetting)
        btnDoneSetting.setOnClickListener {
            // 완료 버튼을 클릭하면 정보를 넘기며 종료
            if (sexOption != "거래 불가"){
                val intent = Intent()
                intent.putExtra("targeting", sexOption)
                intent.putExtra("deadlineData", deadlineTimestamp)
                intent.putExtra("optionSetting", 1)
                setResult(Activity.RESULT_OK, intent)
                onBackPressed()
            } else {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
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