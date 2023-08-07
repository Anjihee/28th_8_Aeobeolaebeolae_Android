package com.surround2023.surround2023.user_login_join

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.surround2023.surround2023.databinding.ActivityLogoutBinding
import com.surround2023.surround2023.mypage.MypageActivity

class LogoutActivity : ComponentActivity(){
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.Yesbtn.setOnClickListener {
            auth.signOut() // 파이어베이스에서 로그아웃
            val loginIntent = Intent(this,LoginActivity::class.java)
            startActivity(loginIntent)
        }

        binding.Nobtn.setOnClickListener {
            val mypageIntent = Intent(this,MypageActivity::class.java)
            startActivity(mypageIntent)
        }
    }
}