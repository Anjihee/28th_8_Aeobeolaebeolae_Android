package com.surround2023.surround2023.market_post

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.surround2023.surround2023.R
import org.w3c.dom.Text
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class MarketPostActivity : AppCompatActivity() {
    private var postId = intent.getStringExtra("postId") // 게시글 Id Data 받아오기

    private var goodsImageView: ImageView = findViewById(R.id.goodsImage)
    private var userProfileView: ImageView = findViewById(R.id.userProfile)
    private var userName: TextView = findViewById(R.id.userName)
    private var userAddress: TextView = findViewById(R.id.userAddress)
    private var targeting: TextView = findViewById(R.id.targeting)
    private var postTitle: TextView = findViewById(R.id.postTitle)
    private var postCategory: TextView = findViewById(R.id.postCategory)
    private var postTime: TextView = findViewById(R.id.postTime)
    private var postContent: TextView = findViewById(R.id.postText)
    private var postDeadline : TextView = findViewById(R.id.textDeadline)
    private var startPrice: TextView = findViewById(R.id.startPrice)

    private val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_post)

        val postInfo = db.collection("Market").document(postId.toString())

        postInfo.get()
            .addOnSuccessListener { document -> // 게시글 정보 로딩 성공
                if (document != null) {
                    val goodsImageUrl = document.getString("postImageUrl")
                    val userRef = document.getDocumentReference("userRef")
                    val title = document.getString("postTitle")
                    val category = document.getString("category")
                    val time = document.getTimestamp("postDate")
                    val content = document.getString("postContent")
                    val manOnly = document.getBoolean("isManOnly")
                    val womanOnly = document.getBoolean("isWomanOnly")
                    val due = document.getTimestamp("postDue")
                    val price = document.getString("price")

                    Glide.with(this)
                        .load(goodsImageUrl) // 게시글 이미지
                        .placeholder(R.drawable.ic_logo) // 로딩 중이나 에러 시 보여줄 기본 이미지
                        .into(goodsImageView) // 표시할 ImageView

                    if (userRef != null) {
                        userRef.get()
                            .addOnSuccessListener { userInfo ->
                                val name = userInfo.getString("userName")
                                val profileUrl = userInfo.getString("userProfileImageUrl")
                                val address = userInfo.getString("userAddress")

                                Glide.with(this)
                                    .load(profileUrl)
                                    .placeholder(R.drawable.icon) // 로딩 중이나 에러 시 보여줄 기본 이미지
                                    .into(userProfileView) // 표시할 ImageView

                                userName.text = name.toString()
                            }
                    } else {
                        // 사용자 정보를 불러올 수 없습니다.
                    }

                    if (manOnly == false && womanOnly == true) {
                        targeting.text = "여성만 거래"
                    } else if (manOnly == true && womanOnly == false) {
                        targeting.text = "남성만 거래"
                    } else {
                        targeting.text = "모두 거래 가능"
                    }

                    postTitle.text = title.toString()
                    postCategory.text = category.toString()
                    postTime.text = time.toString()
                    postContent.text = content.toString()

                    startPrice.text = "1인 " + price.toString()

                    // 마감일 타임스탬프 변환
                    val dueInstant = due?.toDate()?.toInstant()
                    val currentInstant = Instant.now() // 현재 시간
                    val currentDateTime = LocalDateTime.ofInstant(currentInstant, ZoneId.systemDefault())
                    val duration = Duration.between(currentInstant, dueInstant)
                    val days = duration.toDays()
                    postDeadline.text = "${days}일 전" // 마감일 보이기
                }
            }
            .addOnFailureListener { e ->
                // 게시글 정보 로딩 실패
            }

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