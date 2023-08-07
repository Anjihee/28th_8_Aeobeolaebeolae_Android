package com.surround2023.surround2023.posting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.surround2023.surround2023.R

class CommunityPostingActivity : AppCompatActivity() {

    private lateinit var btnAddPhoto: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var photoNum: TextView

    private var photoCount = 0

    private val PICK_IMAGE_REQUEST = 1 // 이미지 선택 요청 코드

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_posting)

        // 소프트키(네비게이션 바), 상태바를 숨기기 위한 플래그 설정
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        btnAddPhoto = findViewById(R.id.btnAddPhoto)
        photoView = findViewById(R.id.photoView)
        photoNum = findViewById(R.id.photoNum)

        btnAddPhoto.setOnClickListener {
            if (photoCount < 10) { // 사진 첨부 버튼 클릭 시 처리(10장 미만)
                openGallery()
            } else {
                Toast.makeText(this, "더 이상 사진을 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() { // 갤러리 열기
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // 갤러리에서 사진 선택 후 실행
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage: Uri = data.data!!
            photoView.setImageURI(selectedImage)
            photoView.visibility = ImageView.VISIBLE

            photoCount++
            photoNum.text = "$photoCount // 10"
        }
    }
}