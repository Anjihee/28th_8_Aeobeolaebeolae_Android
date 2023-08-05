package com.surround2023.surround2023.user_login_join

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.kakao.sdk.auth.AuthApi
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import com.surround2023.surround2023.databinding.ActivityLoginBinding
import com.surround2023.surround2023.home.HomeActivity
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val uid: String? = null,
    val userEmail: String? = null,
    val userPw: String? = null,
    val userName: String? = null
)

class LoginActivity : ComponentActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val join_intent = Intent(this, JoinActivity::class.java) //가입 화면
        val home_intent = Intent(this, HomeActivity::class.java)


        binding.btnLogin.setOnClickListener{
            val email = binding.Email.getText().toString()
            val password = binding.PW.getText().toString()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 로그인에 성공한 경우
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            val uid = user.uid
                            val userName = user.displayName
                            val userEmail = user.email

                            // Firestore 데이터 모델에 사용자 정보 저장
                            val userToSave = User(uid, userName, userEmail)

                            // Firestore에 데이터 저장
                            val db = FirebaseFirestore.getInstance()
                            db.collection("users")
                                .document(uid)
                                .set(userToSave)
                                .addOnSuccessListener {
                                    // 저장 성공
                                    Log.d(TAG, "사용자 정보가 Firestore에 저장되었습니다.")
                                }
                                .addOnFailureListener { e ->
                                    // 저장 실패
                                    Log.w(TAG, "사용자 정보 저장에 실패했습니다.", e)
                                }
                        }

                        // 사용자 정보를 확인하거나 화면을 전환하는 등의 작업 수행
                        startActivity(home_intent)
                    } else {
                        // 로그인에 실패한 경우
                        val exception = task.exception
                        // 에러 메시지를 확인하거나 적절한 에러 처리 수행
                        Toast.makeText(this@LoginActivity,
                            "로그인에 실패하였습니다. 다시 시도해 주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }
        binding.KakaoLoginButton.setOnClickListener {
            kakaoLogin()
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
                    val home_intent = Intent(this, HomeActivity::class.java)
                    startActivity(home_intent)
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
                    val home_intent = Intent(this, HomeActivity::class.java)
                    startActivity(home_intent)
                }
            }
        }
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.e(TAG, "사용자 정보 요청 성공 : $user")
            }
        }
    }

}
