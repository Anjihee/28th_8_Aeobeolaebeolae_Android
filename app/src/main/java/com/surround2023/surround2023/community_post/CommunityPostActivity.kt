package com.surround2023.surround2023.community_post

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.surround2023.surround2023.R

data class CommentData(val userName: String?, val time: Timestamp, val comment: String?, val profileId: String?)

class CommunityPostActivity : AppCompatActivity() {
    private val postId: String = "" // 불러 오려는 댓글의 소속 글

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_post)

        val btnLike: ImageButton = findViewById(R.id.btnLike)
        btnLike.setOnClickListener{// 좋아요 버튼 기능 구현
            btnLike.isSelected = !btnLike.isSelected
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