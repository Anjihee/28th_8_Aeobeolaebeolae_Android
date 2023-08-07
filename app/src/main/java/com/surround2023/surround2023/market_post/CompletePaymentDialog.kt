package com.surround2023.surround2023.market_post

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.surround2023.surround2023.R
import com.surround2023.surround2023.home.HomeActivity
import com.surround2023.surround2023.user_login_join.UserSingleton
import org.w3c.dom.Text
import java.text.NumberFormat

class CompletePaymentDialog(context: Context, private val postId: String) : Dialog(context) {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_complete_payment) // 다이얼로그 레이아웃 설정

        val postInfo = db.collection("Market").document(postId) // post Data 받아오기

        val textUserName :TextView = findViewById(R.id.textUserName)
        val totalDiscountPrice: TextView = findViewById(R.id.textDiscountPrice)
        val priceAverage : TextView = findViewById(R.id.priceAverage)
        val priceSurround : TextView = findViewById(R.id.priceSurrond)

        postInfo.get()
            .addOnSuccessListener { document ->
                val surroundP = document.getLong("price")?.toInt()
                val averageP = document.getLong("averagePrice")?.toInt()
                var discountP = 0

                priceAverage.text = "${averageP}원"
                priceSurround.text = "${surroundP}원"

                if (surroundP != null && averageP != null) {
                    discountP = averageP.toInt() - surroundP
                    val formattedDiscountP = NumberFormat.getNumberInstance().format(discountP)
                    totalDiscountPrice.text = "${formattedDiscountP}원"
                } else {
                    totalDiscountPrice.text = "N원"
                }

            }
            .addOnFailureListener { e ->
                // 실패
            }

        var userData = UserSingleton.getInstance().getUserData() // 유저 이름 불러오기
        userData?.let {
            val userName = it.userName
            textUserName.text = userName
        }


        setCancelable(false) // 다른 화면 터치나 뒤로가기 버튼을 통해 창 닫히지 않게 설정

        val textDisCountPrice : TextView = findViewById(R.id.textDiscountPrice)


        val btnPopupClose = findViewById<TextView>(R.id.btnPopupClose)
        btnPopupClose.setOnClickListener{ // 확인 버튼 누르면 글 화면으로 돌아가기
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
            dismiss()
        }
    }
}