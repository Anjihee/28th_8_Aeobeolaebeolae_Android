package com.surround2023.surround2023.user_login_join

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.surround2023.surround2023.databinding.ActivityJoinBinding
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val Email: String? = null,
    val uid: String? = null,
    val userName: String? = null,
    val gender: String? = null
)

class JoinActivity : ComponentActivity() {
    private lateinit var binding: ActivityJoinBinding
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance() //회원가입 파이어베이스 연동
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    object FirebaseID { //파이어베이스 회원가입 정보
        const val email = "email"
        const val password = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val login_intent = Intent(this, LoginActivity::class.java) //가입 화면

        // 비밀번호 재확인 일치여부
        binding.PW2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val PW = binding.PW.text.toString()
                val PW2 = binding.PW2.text.toString()
                Log.d("PW",PW)
                Log.d("PW2",PW2)
                if (PW==PW2) {
                    binding.textPWConfirm.setText("비밀번호가 일치합니다.")
                    binding.textPWConfirm.setTextColor(Color.parseColor("#97BF04"))
                    // 가입하기 버튼 활성화
                    binding.btnJoin.isEnabled = true
                } else {
                    binding.textPWConfirm.setText("비밀번호가 일치하지 않습니다.")
                    binding.textPWConfirm.setTextColor(Color.parseColor("#FF0000"))
                    // 가입하기 버튼 비활성화
                    binding.btnJoin.isEnabled = false
                }
            }

            //입력하기 전
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textPWConfirm.setText("비밀번호를 다시 한번 입력해주세요.")
                binding.textPWConfirm.setTextColor(Color.parseColor("#97BF04"))
            }

            //텍스트 변화가 있을 시
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        binding.backButton.setOnClickListener() {
            startActivity(login_intent)
        }

        binding.btnJoin.setOnClickListener() {
            if (binding.textPWConfirm.getText()
                    .equals("비밀번호가 일치합니다.") && binding.Nickname.getText()
                    .isNotEmpty() && (binding.Female.isChecked() || binding.Male.isChecked())
            ) {
                signUpWithEmail(binding.Email.toString(), binding.PW.toString())
            }

             else {
                Toast.makeText(this@JoinActivity, "이메일과 비밀번호를 정확히 입력해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun signUpWithEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(
            binding.Email.text.toString(),
            binding.PW.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = mAuth.currentUser
                var gender = ""
                if (binding.Female.isChecked()) {
                    gender = "Female"
                } else if (binding.Male.isChecked()) {
                    gender = "Male"
                }

                val email = user?.email
                val uid = user?.uid
                val userName = binding.Nickname.text.toString()

                // Firestore 데이터 모델에 사용자 정보 저장
                val userToSave = User(email, uid, userName, gender)

                // Firestore에 데이터 저장
                val db = FirebaseFirestore.getInstance()
                db.collection("User")
                    .document(email!!)
                    .set(userToSave)
                    .addOnSuccessListener {
                        // 저장 성공
                        Log.d(ContentValues.TAG, "사용자 정보가 Firestore에 저장되었습니다.")
                    }
                    .addOnFailureListener { e ->
                        // 저장 실패
                        Log.w(ContentValues.TAG, "사용자 정보 저장에 실패했습니다.", e)
                    }

                val Login_intent = Intent(this@JoinActivity, LoginActivity::class.java)
                // LoginActivity로 이동하는 코드 추가 (생략)
                startActivity(Login_intent)
                finish()
            } else {
                Toast.makeText(
                    this@JoinActivity,
                    "회원가입에 실패하였습니다. 다시 시도해 주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}