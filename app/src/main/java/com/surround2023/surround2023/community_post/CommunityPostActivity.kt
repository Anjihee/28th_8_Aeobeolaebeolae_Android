package com.surround2023.surround2023.community_post

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.surround2023.surround2023.R
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class CommentData(val userName: String?, val time: Timestamp, val comment: String?, val profileId: String?)

class CommunityPostActivity : AppCompatActivity() {
    private lateinit var postId : String
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_post)

        // 소프트키(네비게이션 바), 상태바를 숨기기 위한 플래그 설정
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            // 뒤로가기 버튼을 클릭하면 종료
            onBackPressed()
        }

        postId = intent.getStringExtra("postId").toString() // 게시글 Id Data 받아오기

        val goodsImageView: ImageView = findViewById(R.id.goodsImage)
        val userProfileView: ImageView = findViewById(R.id.userProfile)
        val userName: TextView = findViewById(R.id.userName)
        val userAddress: TextView = findViewById(R.id.userAddress)
        val postTitle: TextView = findViewById(R.id.postTitle)
        val postCategory: TextView = findViewById(R.id.postCategory)
        val postTime: TextView = findViewById(R.id.postTime)
        val postContent: TextView = findViewById(R.id.postContents)
        val likeNum: TextView = findViewById(R.id.likeNum)
        val commentNum: TextView = findViewById(R.id.commentsNum)

        val postInfo = db.collection("Community").document(postId)

        postInfo.get()
            .addOnSuccessListener { document -> // 게시글 정보 로딩 성공
                if (document != null) {
                    val goodsImageUrl = document.getString("postImageUrl")
                    val userRef = document.getDocumentReference("userRef")
                    val title = document.getString("postTitle")
                    val category = document.getString("category")
                    val time = document.getTimestamp("postDate")
                    val content = document.getString("postContent")

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
                                userAddress.text = address.toString()
                            }
                    } else {
                        // 사용자 정보를 불러올 수 없습니다.
                    }

                    val currentInstant = Instant.now() // 현재 시간
                    val currentDateTime = LocalDateTime.ofInstant(currentInstant, ZoneId.systemDefault())

                    postTitle.text = title.toString()
                    postCategory.text = category.toString()

                    // 작성일 타임스탬프 변환
                    val timeInstant = time?.toDate()?.toInstant()
                    val durationPost = Duration.between(currentInstant, timeInstant)
                    val daysPost = durationPost.toDays()
                    postTime.text = "${daysPost}일 전"

                    postContent.text = content.toString()

                    val like = document.getLong("likeNum")?.toString()
                    val coms = document.getLong("commentNum")?.toString()

                    if (like != null) {
                        likeNum.text = like
                    } else {
                        likeNum.text = "0"
                    }
                    if (coms != null) {
                        commentNum.text = coms
                    } else {
                        commentNum.text = "0"
                    }

                }
            }
            .addOnFailureListener { e ->
                // 게시글 정보 로딩 실패
            }


        val btnLike: ImageButton = findViewById(R.id.btnLike)
        var pressedLike = 0
        btnLike.setOnClickListener{ // 좋아요 버튼 기능 구현
            btnLike.isSelected = !btnLike.isSelected
            if (pressedLike == 0 && btnLike.isSelected == true) {
                val likeP = likeNum.text.toString().toInt()
                val likeN = likeP + 1
                likeNum.text = likeN.toString()
                pressedLike = 1
            } else if (pressedLike == 1 && btnLike.isSelected == false) {
                val likeP = likeNum.text.toString().toInt()
                val likeN = likeP - 1
                likeNum.text = likeN.toString()
                pressedLike = 0
            }
        }

        val commentList = createCommentList() // 댓글 데이터 -> 리스트 생성
        val commentListView = findViewById<RecyclerView>(R.id.commentsList) // 액티비티의 댓글 리사이클러뷰
        val commentListAdapter = CommentListAdapter(this, commentList) // 생성된 댓글 리스트 -> ListAdapter 클래스
        commentListView.adapter = commentListAdapter
        commentListAdapter.notifyDataSetChanged()
        // LinearLayoutManager를 사용하여 세로로 아이템들을 배치하도록 설정
        commentListView.layoutManager = LinearLayoutManager(this)
    }

    fun createCommentList(): List<CommentData> { // 댓글 DB 받아와 리스트 생성하는 메서드
        val commentDataList = mutableListOf<CommentData>()

        val commentsCollection = db.collection("Community").document(postId).collection("comments")

        commentsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING) // 댓글 달린 순으로 정렬
            .get()
            .addOnSuccessListener { result -> // 성공
                for (document in result){ // 받아들인 정보 저장하여 CommentData 리스트에 추가
                    val userName = document.getString("commentUserName")
                    val time = document.getTimestamp("commentDate")
                    val content = document.getString("commentContent")
                    val profileId = document.getString("commentUserProfileUrl")

                    val comment = time?.let { CommentData(userName, it, content, profileId) }
                    comment?.let { commentDataList.add(it) }
                }
            }
            .addOnFailureListener { e -> // 실패
                Log.w(ContentValues.TAG, "댓글 데이터 불러오기 실패", e)
            }

        return commentDataList
    }
}