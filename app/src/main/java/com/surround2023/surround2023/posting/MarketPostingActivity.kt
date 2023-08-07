package com.surround2023.surround2023.posting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.surround2023.surround2023.R
import java.util.Date

data class Post(
    var title: String? = null,
    var category: String? = null,
    var isManOnly: Boolean = false,
    var isWomanOnly: Boolean = false,
    var price: String? = null,
    var content: String? = null,
    var date: String? = null,
    var due: String? = null
)
class MarketPostingActivity : AppCompatActivity() {
    val postData = Post() // Post 인스턴스 생성

    private lateinit var btnAddPhoto: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var photoNum: TextView

    private var photoCount = 0

    private val PICK_IMAGE_REQUEST = 1 // 이미지 선택 요청 코드

    private val REQUEST_CODE_B = 100

    private lateinit var postTitle: EditText
    private lateinit var postContent: EditText
    private lateinit var price: EditText

    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_posting)

        btnAddPhoto = findViewById(R.id.btnAddPhoto)
        photoView = findViewById(R.id.photoView)
        photoNum = findViewById(R.id.photoNum)

        val btnFinished : TextView = findViewById(R.id.btnFinish)

        btnAddPhoto.setOnClickListener {
            if (photoCount < 2) { // 사진 첨부 버튼 클릭 시 처리(2장 미만)
                openGallery()
            } else {
                Toast.makeText(this, "더 이상 사진을 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        val btnSettingOption: Button = findViewById(R.id.optionSettingBtn)
        btnSettingOption.setOnClickListener {
            // 버튼을 클릭하면 다른 액티비티를 열도록 인텐트 생성
            val intent = Intent(this@MarketPostingActivity, MarketOptionSettingActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_B)
        }

        btnFinished.setOnClickListener {

            savePost(postData)
            onBackPressed() // 돌아가기
        }
    }

    fun savePost(postInfo: Post) { // DB에 데이터 쓰기
        val titleData = postInfo.title
        val categoryData = postInfo.category
        val manOnly = postInfo.isManOnly
        val womanOnly = postInfo.isWomanOnly
        val priceData = postInfo.price
        val contentData = postInfo.content
        val dateData = postInfo.date
        val dueData = postInfo.due

        // 데이터 쓰기
        // db.collection("market")
            /* .addOnSuccessListener {
                // 저장 성공
                Log.d(ContentValues.TAG, "사용자 정보가 Firestore에 저장되었습니다.")
            }
            .addOnFailureListener { e ->
                // 저장 실패
                Log.w(ContentValues.TAG, "사용자 정보 저장에 실패했습니다.", e)
            } */
    }

    private fun openGallery() { // 갤러리 열기
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 갤러리에서 사진 선택 후 실행
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage: Uri = data.data!!
            photoView.setImageURI(selectedImage)
            photoView.visibility = ImageView.VISIBLE

            photoCount++
            photoNum.text = "$photoCount // 2"
        }

        if (requestCode == REQUEST_CODE_B) {
            if (resultCode == Activity.RESULT_OK) {
                val targetting = data?.getStringExtra("targetting")
                val deadlineData = data?.getStringExtra("deadlineData")
                val optionSetting = data?.getStringExtra("optionSetting")

                if (targetting == "여성만 거래") {
                    postData.isManOnly = false
                    postData.isWomanOnly = true
                } else if (targetting == "남성만 거래") {
                    postData.isManOnly = true
                    postData.isWomanOnly = false
                } else {
                    postData.isManOnly = false
                    postData.isWomanOnly = false
                }

                postData.due = deadlineData
            }
        }
    }
}
