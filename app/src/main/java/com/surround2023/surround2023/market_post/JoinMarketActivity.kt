package com.surround2023.surround2023.market_post

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.surround2023.surround2023.R
import java.text.NumberFormat
import java.text.SimpleDateFormat

class JoinMarketActivity : AppCompatActivity() {
    private lateinit var postId: String
    private val db = FirebaseFirestore.getInstance()

    private lateinit var radioButtons: Array<RadioButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_market)

        // 소프트키(네비게이션 바), 상태바를 숨기기 위한 플래그 설정
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        postId = intent.getStringExtra("postId").toString() // 게시글 Id Data 받아오기
        val joinWay = intent.getStringExtra("joinWay").toString() // 수령 방법 Data 받아오기

        val goodsImageView : ImageView = findViewById(R.id.goodsImage)
        val perPrice : TextView = findViewById(R.id.perPrice)
        val postTitle : TextView = findViewById(R.id.postTitle)
        val deadline : TextView = findViewById(R.id.deadLine)
        val userName : TextView = findViewById(R.id.userName)
        val userMembership : TextView = findViewById(R.id.userMembership)
        val userAddress : TextView = findViewById(R.id.userAddress)
        val joinWayView : TextView = findViewById(R.id.joinWay)
        val editUsePoint : EditText = findViewById(R.id.editUsePoint)
        val btnUse: Button = findViewById(R.id.btnUse)
        val goodsPrice: TextView = findViewById(R.id.goodsPrice)
        val deliverPrice: TextView = findViewById(R.id.deliverPrice)
        val usePoint: TextView = findViewById(R.id.usePoint)
        val earnPoint: TextView = findViewById(R.id.earnPoint)
        val totalPrice: TextView = findViewById(R.id.totalPrice)

        val postInfo = db.collection("Market").document(postId) // post Data 받아오기

        postInfo.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val postImageUrl = document.getString("postImageUrl")
                    val userRef = document.getDocumentReference("userRef")
                    val title = document.getString("postTitle")
                    val due = document.getTimestamp("postDue")
                    val price = document.getLong("price")?.toInt()

                    Glide.with(this)
                        .load(postImageUrl) // 게시글 이미지
                        .placeholder(R.drawable.ic_logo) // 로딩 중이나 에러 시 보여줄 기본 이미지
                        .into(goodsImageView) // 표시할 ImageView

                    //천 단위마다 쉼표를 포함하여 정수를 포맷팅
                    val formattedPrice = NumberFormat.getNumberInstance().format(price)
                    perPrice.text = "${formattedPrice}원"

                    postTitle.text = title

                    // 마감일 날짜만 추출해서 보이기
                    val dueDate = due?.toDate()
                    val dateFormat = SimpleDateFormat("yyyy.MM.dd")
                    deadline.text = dateFormat.format(dueDate)

                    if (userRef != null) {
                        userRef.get()
                            .addOnSuccessListener { userInfo ->
                                val name = userInfo.getString("userName")
                                val membership = userInfo.getString("userMembership")
                                val address = userInfo.getString("userLocation")

                                userName.text = name.toString()
                                userMembership.text = membership.toString()
                                userAddress.text = address.toString()
                            }
                    } else {
                        // 사용자 정보를 불러올 수 없습니다.
                    }

                    joinWayView.text = joinWay

                    // 사용할 공구포인트 기능
                    var useP = "0"
                    btnUse.setOnClickListener {
                        useP = editUsePoint.text.toString()
                    }

                    goodsPrice.text = "${formattedPrice}원"

                    // 수령 방법에 따라 배송료
                    var deliverP = 0
                    if (joinWay == "직거래") {
                        deliverPrice.text = "+0원"
                        deliverP = 0
                    } else {
                        deliverPrice.text = "+1,500원"
                        deliverP = 1500
                    }

                    // 공구포인트 사용
                    val formattedUse = NumberFormat.getNumberInstance().format(useP.toInt())
                    usePoint.text = "-${formattedUse}원"

                    // 예상 적립 포인트 계산 및 보여주기
                    val earnP = price?.div(100)
                    earnPoint.text = "${earnP}원"

                    // 최종 계산 가격 계산 및 보여주기
                    val totalP = price?.plus(deliverP)
                    val formattedTotal = NumberFormat.getNumberInstance().format(totalP)
                    totalPrice.text = "${formattedTotal}원"
                }
            }
            .addOnFailureListener {
                // 실패
            }
        
        val btnBack : ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener { // '뒤로가기' 버튼 클릭 시
           onBackPressed()
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
        val agreeAllBtn : CheckBox = findViewById(R.id.agreeAll)
        payingBtn.setOnClickListener {
            if (agreeAllBtn.isChecked == true) {
                // 결제 완료 창 Dialog 띄우기
                val completeDialog = CompletePaymentDialog(this, postId)
                completeDialog.show()
            } else {
                Toast.makeText(this, "이용 약관에 동의해 주세요.", Toast.LENGTH_SHORT).show()
            }

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

    override fun onBackPressed() { // 뒤로가기
        super.onBackPressed()
        finish() // 현재 액티비티 종료
    }
}