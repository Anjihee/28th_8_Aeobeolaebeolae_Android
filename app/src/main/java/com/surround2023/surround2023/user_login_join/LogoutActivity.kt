package com.surround2023.surround2023.user_login_join

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.surround2023.surround2023.databinding.ActivityLogoutBinding
import com.surround2023.surround2023.mypage.MypageActivity

class LogoutActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Yesbtn.setOnClickListener {
            val loginIntent = Intent(this,LoginActivity::class.java)
            startActivity(loginIntent)
        }

        binding.Nobtn.setOnClickListener {
            val mypageIntent = Intent(this,MypageActivity::class.java)
            startActivity(mypageIntent)
        }
    }
}