package com.surround2023.surround2023.splash

import android.app.Activity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.surround2023.surround2023.R
import com.surround2023.surround2023.databinding.ActivitySplashScreenBinding

class SplashScreenActivity:Activity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        splashAnimation()

        setContentView(binding.root)


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
//                startLoginActivity()
            }
        })

    }

//    private fun startLoginActivity() {
//        val intent = Intent(this, LoginActivity::class.java)
//        startActivity(intent)
//        finish() // Optional: If you want to finish the splash screen activity when starting the main activity
//    }


}