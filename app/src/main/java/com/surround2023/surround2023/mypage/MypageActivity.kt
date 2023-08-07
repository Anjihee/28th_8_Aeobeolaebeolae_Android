package com.surround2023.surround2023.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.surround2023.surround2023.R
import com.surround2023.surround2023.community_log.CommunityLogActivity
import com.surround2023.surround2023.databinding.ActivityMypageBinding
import com.surround2023.surround2023.home.HomeActivity
import com.surround2023.surround2023.market_log.MarketLogJoinActivity
import com.surround2023.surround2023.market_log.MarketLogOpenActivity
import com.surround2023.surround2023.posting.MarketPostingActivity
import com.surround2023.surround2023.set_location.SetLocationActivity
import com.surround2023.surround2023.user_login_join.LogoutActivity
import com.surround2023.surround2023.user_login_join.UserSingleton

class MypageActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMypageBinding
    //하단 Nav 와 관련된 변수
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)



        var userData = UserSingleton.getInstance().getUserData()
        val mypageUsernameTextView: TextView = findViewById(R.id.mypageusername)
        userData?.let {
            val userName = it.userName
            mypageUsernameTextView.text = userName
        }

        // mypagebtn1 버튼 클릭시 내가 진행한 공구
        val mypagebtn1: Button = findViewById(R.id.mypagebtn1)
        mypagebtn1.setOnClickListener {
            val intent = Intent(this, MarketLogOpenActivity::class.java)
            startActivity(intent)
        }

        // mypagebtn2 버튼 클릭시 내가 참여 공구
        val mypagebtn2: Button = findViewById(R.id.mypagebtn2)
        mypagebtn2.setOnClickListener {
            val intent = Intent(this, MarketLogJoinActivity::class.java)
            startActivity(intent)
        }


        //커뮤니티 활동관리로 이동
        val communitylogtext: TextView = findViewById(R.id.communitylogtext)
        communitylogtext.setOnClickListener {
            val intent = Intent(this, CommunityLogActivity::class.java)
            startActivity(intent)
        }


        //동네 설정으로 이동
        val setlocationtext: TextView = findViewById(R.id.setlocationtext)
        setlocationtext.setOnClickListener {
            val intent = Intent(this, SetLocationActivity::class.java)
            startActivity(intent)
        }


        //로그아웃으로 이동
        val logouttext: TextView = findViewById(R.id.logouttext)
        logouttext.setOnClickListener {
            val intent = Intent(this, LogoutActivity::class.java)
            startActivity(intent)
        }


        //하단바 기능
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
                R.id.menu_favorite -> {
                    //즐겨찾기로 이동
                    true
                }

                R.id.menu_addPost -> {
                    //공동구매 글쓰기로 이동
                    val intent = Intent(this, MarketPostingActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.menu_mypage -> {
                    //마이페이지로 이동 (현재 액티비티이므로 아무 동작 없음)
                    true
                }
                // 다른 메뉴 아이템에 대한 처리 추가 (필요에 따라 다른 Activity로 이동할 수 있음)
                else -> false
            }
        }
    }


}
