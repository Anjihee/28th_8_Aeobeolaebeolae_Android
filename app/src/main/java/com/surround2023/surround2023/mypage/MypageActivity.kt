package com.surround2023.surround2023.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.surround2023.surround2023.R
import com.surround2023.surround2023.databinding.ActivityHomeBinding
import com.surround2023.surround2023.home.HomeActivity
import com.surround2023.surround2023.market_log.MarketLogOpenActivity

class MypageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    //하단 Nav 와 관련된 변수
    private lateinit var bottomNavView: BottomNavigationView

//    val mypagebtn1: Button = findViewById(R.id.mypagebtn1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)


//        mypagebtn1.setOnClickListener {
//            val intent = Intent(this, MarketLogOpenActivity::class.java)
//            startActivity(intent)
//        }
//


        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //        ---------------------BottomNavigationView에 대한 기능 ---------------
        bottomNavView = binding.bottomNavView
        bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                //해당 액티비티로 이동
                R.id.menu_home -> {
                    // "menu_home" 아이템 클릭 시 HomeActivity로 이동
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_favorite ->{
                    //즐겨찾기로 이동
                    true
                }

                R.id.menu_addPost ->{
                    //공동구매 글쓰기로 이동
                    true
                }

                R.id.menu_mypage ->{
                    //마이페이지로 이동
                    true
                }
                // 다른 메뉴 아이템에 대한 처리 추가 (필요에 따라 다른 Activity로 이동할 수 있음)
                else -> false
            }
        }
    }

}