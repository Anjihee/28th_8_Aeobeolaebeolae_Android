package com.surround2023.surround2023.user_login_join

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.surround2023.surround2023.databinding.ActivityLoginBinding

class LogoutActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}