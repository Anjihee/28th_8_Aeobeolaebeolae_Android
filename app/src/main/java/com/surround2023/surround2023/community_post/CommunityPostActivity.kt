package com.surround2023.surround2023.community_post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.surround2023.surround2023.R

data class CommentData(val userName: String, val time: String, val comment: String, val profileId: Int)

class CommunityPostActivity : AppCompatActivity() {
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

    private fun createCommentList(): List<CommentData> {
        // 더미 데이터를 생성하여 CommentData 객체를 생성하고 리스트에 담아 반환하는 메서드
        val dummyDataList = mutableListOf<CommentData>()

        // 더미 데이터 추가
        dummyDataList.add(CommentData("강형욱", "50분 전", "아웅 커여워!!", R.drawable.person))
        dummyDataList.add(CommentData("뽀삐누나", "20분 전", "오쪼쪼 말랑따끈강쥐", R.drawable.person))
        // 추가적인 더미 데이터들을 생성하여 리스트에 추가할 수 있습니다.

        return dummyDataList
    }
}