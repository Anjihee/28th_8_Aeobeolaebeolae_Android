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
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.surround2023.surround2023.databinding.ActivityLoginBinding
import com.surround2023.surround2023.home.HomeActivity

class LoginActivity : ComponentActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val joinIntent = Intent(this, JoinActivity::class.java) //가입 화면


        //hide navigation bar
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        }

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("User")

        binding.btnLogin.setOnClickListener {
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
                                    val homeIntent = Intent(this, HomeActivity::class.java) //홈 화면
                                    startActivity(homeIntent)
                                } else {
                                    // 문서가 존재하지 않거나 데이터를 읽어오는 데에 실패한 경우
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "로그인에 실패하였습니다. 다시 시도해 주세요.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .addOnFailureListener { exception ->
                                // 데이터를 읽어오는 데에 실패한 경우
                                Toast.makeText(
                                    this@LoginActivity,
                                    "로그인에 실패하였습니다. 다시 시도해 주세요.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        // 로그인에 실패한 경우
                        Toast.makeText(
                            this@LoginActivity,
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
            startActivity(joinIntent)
        }
    }

    private fun kakaoLogin() {
        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)
        Log.d("LOGIN", "카카오계정으로 로그인 진입")
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.d("LOGIN", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.d("LOGIN", "카카오계정으로 로그인 성공 ${token.accessToken}")
                val homeIntent = Intent(this, HomeActivity::class.java) //홈 화면
                startActivity(homeIntent)
                finish()
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("LOGIN", "카카오톡으로 로그인 실패", error)
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i("LOGIN", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    val homeIntent = Intent(this, HomeActivity::class.java) //홈 화면
                    startActivity(homeIntent)
                    finish()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }
}
