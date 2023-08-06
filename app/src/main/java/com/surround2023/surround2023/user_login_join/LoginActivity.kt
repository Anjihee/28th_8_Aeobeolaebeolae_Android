package com.surround2023.surround2023.user_login_join

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.surround2023.surround2023.databinding.ActivityLoginBinding
import com.surround2023.surround2023.home.HomeActivity

class LoginActivity : ComponentActivity() {

    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val join_intent = Intent(this, JoinActivity::class.java) //가입 화면
        val home_intent = Intent(this, HomeActivity::class.java) //홈 화면

        //hide navigation bar
        window.decorView.apply{
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        }

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("User")

        binding.btnLogin.setOnClickListener{
            val email = binding.Email.text.toString()
            val password = binding.PW.text.toString()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 로그인에 성공한 경우 사용자 정보를 확인하거나 화면을 전환하는 등의 작업 수행
                        collectionRef.document(email).get()
                            .addOnSuccessListener { document ->
                                if (document != null && document.exists()) {
                                    // 문서가 존재하고 데이터를 읽어올 수 있는 경우
                                    val data = document.data
                                    Log.d("datasurround123", data.toString())
                                    startActivity(home_intent)
                                } else {
                                    // 문서가 존재하지 않거나 데이터를 읽어오는 데에 실패한 경우
                                    Toast.makeText(this@LoginActivity,
                                        "로그인에 실패하였습니다. 다시 시도해 주세요.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .addOnFailureListener { exception ->
                                // 데이터를 읽어오는 데에 실패한 경우
                                Toast.makeText(this@LoginActivity,
                                    "로그인에 실패하였습니다. 다시 시도해 주세요.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        // 로그인에 실패한 경우
                        Toast.makeText(this@LoginActivity,
                            "이메일과 비밀번호를 다시 확인해 주세요.",
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
                    token?.accessToken?.let {
                        Log.d("DEBUG", "카카오톡 토큰 $it")
                        loginInFirebase()
                    }
                    val home_intent = Intent(this, HomeActivity::class.java)
                    startActivity(home_intent)
                }
            }
        }
        //카카오계정 로그인(카카오톡 설치가 안 되어있을 시)
        else {
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

    private fun loginInFirebase() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("DEBUG", "카카오톡 사용자 정보가져오기 에러 ${error.message}")
            } else {
                Log.d("DEBUG", "카카오톡 사용자 정보가져오기 success.")
                // 파이어베이스 유저 생성 (이메일로 회원가입)
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    user?.kakaoAccount?.email ?: "",
                    "${user?.id}"
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("DEBUG", "파이어베이스 사용자 생성")
                        // 회원가입 화면으로 이동
                        val join_intent = Intent(this, JoinActivity::class.java) //가입 화면
                        startActivity(join_intent)
                        // 창닫기
                        finish()
                    } else {
                        Log.e("DEBUG", "파이어베이스 사용자 생성 실패 ${task.exception?.message}")
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                            user?.kakaoAccount?.email ?: "",
                            "${user?.id}"
                        )
                        val join_intent = Intent(this, JoinActivity::class.java) //가입 화면
                        startActivity(join_intent)
                    }
                }
            }
        }
    }

}
