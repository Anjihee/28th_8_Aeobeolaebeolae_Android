package com.surround2023.surround2023.market_log

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.surround2023.surround2023.databinding.ActivityMarketLogOpenBinding


class MarketLogOpenActivity : ComponentActivity() {

    val TAG:String ="로그"

    //뷰바인딩
    val binding by lazy{ ActivityMarketLogOpenBinding.inflate(layoutInflater)}

    //데이터를 담을 배열
    var modelList=ArrayList<MarketLogOpenItem>()
    
    //리사이클러뷰 어댑터
    private lateinit var marketLogOpenRecyclerAdapter: MarketLogOpenRecyclerAdapter

    //뷰가 화면에 그려질 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        -----------------리사이클러뷰---------------------------
        Log.d(TAG, "MainActivity - onCreate() called")
        //데이터 넣기 전
        Log.d(TAG, "MainActivity - this.modelList.size: ${this.modelList.size}")

        for (i in 1..5) {
            //RecyclerView의 Item에 들어갈 데이터 넣고
            val marketLogOpenItem = MarketLogOpenItem("상품 $i", "", "1시간 전", "이태원동")
            //RecyclerView에 보여져야 하는 리스트에 item add
            this.modelList.add(marketLogOpenItem)
        }
        //반복문 후 modelList 크기
        Log.d(TAG, "MainActivity - this.modelList.size: ${this.modelList.size}")

        // 어답터 인스턴스 생성
        marketLogOpenRecyclerAdapter = MarketLogOpenRecyclerAdapter()

        marketLogOpenRecyclerAdapter.submitList(this.modelList)

        //[진행중] 리사이클러뷰 설정
        binding.inProgressRecycler.apply {

            //리사이클러뷰 방향 등 설정
            layoutManager =
                LinearLayoutManager(this@MarketLogOpenActivity, LinearLayoutManager.HORIZONTAL, false)

            //item 사이 horizontal 간격 설정
            val horizontalSpaceItemDecoration =
                HorizontalItemDecoration(25, 25) // 가로 간격 크기를 16픽셀로 설정
            binding.inProgressRecycler.addItemDecoration(horizontalSpaceItemDecoration)
            //어답터 장착
            adapter = marketLogOpenRecyclerAdapter
        }

        //[진행완료] 리사이클러뷰 설정
        binding.completedRecycler.apply {

            //리사이클러뷰 방향 설정
            layoutManager =
                LinearLayoutManager(this@MarketLogOpenActivity, LinearLayoutManager.HORIZONTAL, false)

            //item 사이 horizontal 간격 설정
            val horizontalSpaceItemDecoration =
                HorizontalItemDecoration(25, 25) // 가로 간격 크기를 16픽셀로 설정
            binding.completedRecycler.addItemDecoration(horizontalSpaceItemDecoration)

            //item 배경색 투명도 설정
            val itemAlphaDecoration = ItemAlphaDecoration(0.7f)
            binding.completedRecycler.addItemDecoration(itemAlphaDecoration)

            //어답터 장착
            adapter = marketLogOpenRecyclerAdapter
        }

        //------ 하단 네비게이션 바 안보이게
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
    }

}



