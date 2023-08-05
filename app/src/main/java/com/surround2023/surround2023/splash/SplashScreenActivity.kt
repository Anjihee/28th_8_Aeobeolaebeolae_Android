package com.surround2023.surround2023.splash

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.surround2023.surround2023.R
import com.surround2023.surround2023.databinding.ActivitySplashScreenBinding
import com.surround2023.surround2023.user_login_join.LoginActivity

class SplashScreenActivity:Activity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        splashAnimation()

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //hide navigation bar
        window.decorView.apply{
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        }


    }

    private fun splashAnimation(){
        val animationImage = AnimationUtils.loadAnimation(this, R.anim.anim_splash_image)
        val animationText = AnimationUtils.loadAnimation(this, R.anim.anim_splash_text)
        binding.logoIcon.startAnimation(animationImage)
        binding.logoText.startAnimation(animationText)

        animationText.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                startLoginActivity()
            }
        })

    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Optional: If you want to finish the splash screen activity when starting the main activity
    }


}