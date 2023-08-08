package com.surround2023.surround2023.community_log

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.surround2023.surround2023.R
import com.surround2023.surround2023.databinding.ActivityCommunityLogBinding
import com.surround2023.surround2023.home.HomeActivity
import com.surround2023.surround2023.mypage.MypageActivity
import com.surround2023.surround2023.posting.MarketPostingActivity
import com.surround2023.surround2023.user_login_join.UserSingleton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommunityLogActivity : AppCompatActivity() {
    private val TAG = "로그"

    private lateinit var binding: ActivityCommunityLogBinding

    //하단 Nav 와 관련된 변수
    private lateinit var bottomNavView: BottomNavigationView
    val db= FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //뷰바인딩
        binding = ActivityCommunityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "MainActivity - onCreate() called")

        //userData
        // Retrieve the userData.uid from the intent
        val userId = intent.getStringExtra("USER_ID")
        Log.d("CommunityLogActivity", "User ID: $userId")

        // viewPager 변수 선언
        val viewPager: ViewPager2 = binding.viewPager

        db.collection("Community")
            .whereEqualTo("uid",userId)
            .get()
            .addOnSuccessListener {
                val itemList = mutableListOf<MyPostModel>()
                for(doc in it){
                    val postTitle=doc.getString("postTitle")
                    val postImageUrl=doc.getString("postImageUrl")
                    val likeNum=doc.getLong("likeNum")
                    val commentsNum=doc.getLong("commentsNum")
                    val timestamp = doc.getTimestamp("postDate")
                    val postDate = timestamp?.toDate()?.let { formatDate(it) }

                    val item = MyPostModel(
                        postTitle = postTitle,
                        postImageUrl = postImageUrl,
                        likeNum = likeNum?.toInt() ?: 0,
                        commentsNum = commentsNum?.toInt() ?: 0,
                        postDate = postDate

                    )
                    itemList.add(item)
                }
                Log.d("CommunityLogActivity","$itemList")

                // itemList을 어댑터에 설정합니다.
                val viewPagerFragmentAdapter = CommunityLogViewPager2FragmentAdapter(this)
                viewPagerFragmentAdapter.submitList(ArrayList(itemList))
                viewPager.adapter = viewPagerFragmentAdapter
            }


//        ------------------게시글/댓글 탭에 대한 기능 구현 ---------------------------


        val tabLayout: TabLayout = binding.tabLayout
        Log.d(TAG, "CommunityLogActivity - viewBinding: viewPager, tabLayout")

        // 어댑터 설정
        val viewPagerFragmentAdapter =
            CommunityLogViewPager2FragmentAdapter(this)
        viewPager.adapter = viewPagerFragmentAdapter

        // tabLayout, viewPager 연결
        val tabTitles = listOf("게시글", "댓글")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()



//        ---------------------BottomNavigationView에 대한 기능 ---------------
        bottomNavView = binding.bottomNavView
        bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                //해당 액티비티로 이동
                R.id.menu_home -> {
                    // "menu_home" 아이템 클릭 시 HomeActivity로 이동
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_favorite ->{
                    //즐겨찾기로 이동
                    true
                }

                R.id.menu_addPost ->{
                    //공동구매 글쓰기로 이동
                    val intent=Intent(this, MarketPostingActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.menu_mypage ->{
                    //마이페이지로 이동
                    val intent=Intent(this, MypageActivity::class.java)
                    startActivity(intent)
                    true
                }
                // 다른 메뉴 아이템에 대한 처리 추가 (필요에 따라 다른 Activity로 이동할 수 있음)
                else -> false
            }
        }


        //------ 하단 네비게이션 기본 소프트키 안보이게--------------------------
        val uiOptions = window.decorView.systemUiVisibility
        var newUiOptions = uiOptions
        val isImmersiveModeEnabled = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY == uiOptions
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off. ")
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.")
        }
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = newUiOptions

        //--------------- 뒤로가기 버튼 이벤트
        binding.prevBtn.setOnClickListener{
            // SetLocationActivity를 종료하여 이전 화면으로 돌아감
            Log.d(TAG, "CommunityLogActivity is finished")
            finish()
        }

    }


    // 날짜 포맷팅 함수
    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(date)
    }






    //해시키 얻기
//    private fun getAppKeyHash() {
//        try {
//            val info =
//                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                var md: MessageDigest
//                md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                val something = String(Base64.encode(md.digest(), 0))
//                Log.e("Hash key", something)
//            }
//        } catch (e: Exception) {
//
//            Log.e("name not found", e.toString())
//        }
//    }

}

