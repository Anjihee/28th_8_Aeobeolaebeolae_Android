package com.surround2023.surround2023.posting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.surround2023.surround2023.R
import com.surround2023.surround2023.user_login_join.UserSingleton
import java.lang.ref.Reference
import java.util.Date

data class Post(
    var title: String? = null,
    var category: String? = null,
    var isManOnly: Boolean = false,
    var isWomanOnly: Boolean = false,
    var price: Int? = null,
    var content: String? = null,
    var date: Timestamp? = null,
    var due: Timestamp? = null,
    var userRef: DocumentReference? = null

)
class MarketPostingActivity : AppCompatActivity() {
    val postData = Post() // Post 인스턴스 생성

    private lateinit var btnAddPhoto: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var photoNum: TextView

    private var photoCount = 0
    var optionSetting : Int? = 0

    private val PICK_IMAGE_REQUEST = 1 // 이미지 선택 요청 코드

    private val REQUEST_CODE_B = 100

    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_posting)

        btnAddPhoto = findViewById(R.id.btnAddPhoto)
        photoView = findViewById(R.id.photoView)
        photoNum = findViewById(R.id.photoNum)

        val btnClose : ImageButton = findViewById(R.id.btnClose)
        val btnFinished : TextView = findViewById(R.id.btnFinish)

        val postTitle : EditText = findViewById(R.id.marketPostingTitle)
        val postCategory : Spinner = findViewById(R.id.postCategory)
        val startPrice : EditText = findViewById(R.id.marketPostingPrice)
        val postText : EditText = findViewById(R.id.marketPostingContent)

        postData.title = postTitle.text.toString()
        postData.price = startPrice.text.toString().toInt()
        postData.content = postText.text.toString()

        // Spinner에서 항목 선택 시 리스너 등록
        postCategory.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategoty = parent?.getItemAtPosition(position).toString()
                postData.category = selectedCategoty
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 처리
                postData.category = null
            }
        })

        var userData = UserSingleton.getInstance().getUserData() // 유저 이름 불러오기
        userData?.let {
            val userId = it.Email
            val userRefPath = "User/${userId}"
            postData.userRef = db.document(userRefPath)
        }

        btnClose.setOnClickListener{ // 종료 버튼
            onBackPressed()
        }

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
            val postTime: Timestamp = Timestamp(Date()) // 현재 시간 가져오기
            postData.date = postTime // 시간 저장

            savePost(postData)
            onBackPressed() // 돌아가기


        }
    }

    fun savePost(postInfo: Post) { // DB에 데이터 쓰기 메서드
        val titleData = postInfo.title
        val categoryData = postInfo.category
        val manOnly = postInfo.isManOnly
        val womanOnly = postInfo.isWomanOnly
        val priceData = postInfo.price
        val contentData = postInfo.content
        val dateData = postInfo.date
        val dueData = postInfo.due

        val postMarketCollection = db.collection("Market")

        val newPostMarket = hashMapOf(
            "postTitle" to titleData,
            "category" to categoryData,
            "isManOnly" to manOnly,
            "isWomanOnly" to womanOnly,
            "price" to priceData,
            "postContent" to contentData,
            "postDue" to dueData,
            "postDate" to dateData
        )

        // add() 메서드를 사용하여 데이터 추가
        postMarketCollection.add(newPostMarket)
            .addOnSuccessListener { documentReference ->
                // 추가 성공 시 처리
            }
            .addOnFailureListener { e ->
                // 추가 실패 시 처리
            }
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
                val deadlineData = data?.getStringExtra("deadlineData") ?: ""
                optionSetting = data?.getStringExtra("optionSetting")?.toInt()

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

                if(deadlineData != "") {
                    val deadlineTimestamp =
                        Timestamp(Date(deadlineData.toLong())) // 문자열을 Long 타입으로 변환하여 생성
                    postData.due = deadlineTimestamp
                }
            }
        }
    }
}
