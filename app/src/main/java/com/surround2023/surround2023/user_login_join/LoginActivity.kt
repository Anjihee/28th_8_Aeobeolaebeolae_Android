package com.surround2023.surround2023.user_login_join

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.kakao.sdk.common.util.Utility
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.surround2023.surround2023.databinding.ActivityLoginBinding

class LoginActivity : ComponentActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val join_intent = Intent(this, JoinActivity::class.java) //가입 화면

        binding.KakaoLoginButton.setOnClickListener{
            kakaoLogin()
            //Login후 액티비티
        }

        binding.btnJoin.setOnClickListener {
            startActivity(join_intent)
        }
    }
    private fun kakaoLogin() {
        val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패 $error")
            } else if (token != null) {
                Log.e(TAG, "로그인 성공 ${token.accessToken}")
            }
        }
        // 카카오톡 설치 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            // 카카오톡 로그인
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                // 로그인 실패 부분
                if (error != null) {
                    Log.e(TAG, "로그인 실패 $error")
                    // 사용자가 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                        return@loginWithKakaoTalk
                    }
                    // 다른 오류
                    else {
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback) // 카카오 이메일 로그인
                    }
                }
                // 로그인 성공 부분
                else if (token != null) {
                    Log.e(TAG, "로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback) // 카카오 이메일 로그인
        }
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                        //로그인 필요
                    }
                    else {
                        //기타 에러
                    }
                }
                else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                }
            }
        }
        else {
            //로그인 필요
        }
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.e(TAG, "사용자 정보 요청 성공 : $user")
//                binding.txtNickName.text = user.kakaoAccount?.profile?.nickname
//                binding.txtAge.text = user.kakaoAccount?.ageRange.toString()
//                binding.txtEmail.text = user.kakaoAccount?.email
            }
        }
    }
}
