package com.surround2023.surround2023.market

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.surround2023.surround2023.R
import com.surround2023.surround2023.category.BuyCategoryActivity
import com.surround2023.surround2023.databinding.ActivityMarketholderBinding
import com.surround2023.surround2023.databinding.ActivityMarketitemBinding
import com.surround2023.surround2023.home.HomeActivity
import com.surround2023.surround2023.market.MarketPostModel
import com.surround2023.surround2023.market_post.MarketPostActivity
import com.surround2023.surround2023.mypage.MypageActivity
import com.surround2023.surround2023.posting.CommunityPostingActivity
import com.surround2023.surround2023.posting.MarketPostingActivity


class MarketholderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarketholderBinding

    //리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MarketAdapter

    //하단 Nav 와 관련된 변수
    private lateinit var bottomNavView: BottomNavigationView

    //게시글 데이터베이스
    val db = FirebaseFirestore.getInstance()  //Firestore 인스턴스 선언

    private lateinit var button: ImageButton
    private lateinit var editText: EditText


    private val BUY_CATEGORY_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {


        binding = ActivityMarketholderBinding.inflate(layoutInflater)


        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent = Intent(this, CommunityPostingActivity::class.java)
        binding.writeBtn.setOnClickListener{startActivity(intent)}

        val mintent = Intent(this, BuyCategoryActivity::class.java)
        binding.categoryBtn.setOnClickListener{startActivity(mintent)}


        // 소프트키(네비게이션 바), 상태바를 숨기기 위한 플래그 설정
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        // Initialize RecyclerView
        recyclerView = binding.recyclerView

        //리사이클러뷰 레이아웃매니저
        val recyclerLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = recyclerLayoutManager

        // Initialize Adapter
        adapter = MarketAdapter()
        recyclerView.adapter = adapter

        // item 사이 밑줄 추가
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, recyclerLayoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        //populate data
        getMarketData()


        binding.categoryBtn.setOnClickListener {
            val intent = Intent(this, BuyCategoryActivity::class.java)
            startActivityForResult(intent, BUY_CATEGORY_REQUEST_CODE)
            onActivityResult(BUY_CATEGORY_REQUEST_CODE, Activity.RESULT_OK, intent)
        }

        button = findViewById(R.id.searchBtn)
        editText = findViewById(R.id.etSearch)
        button.setOnClickListener {
            val searchKeyword = editText.text.toString().trim()
            searchMarketData(searchKeyword)
        }

        val searchText = intent.getStringExtra("searchText")
        if (!searchText.isNullOrBlank()) {
            searchCommunityData(searchText)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BUY_CATEGORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val category = data?.getStringExtra("category")
            if (category != null) {
                db.collection("Market").whereEqualTo("category", category)
                    .orderBy("postDate", Query.Direction.DESCENDING)
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        if (querySnapshot == null) return@addSnapshotListener

                        val marketItemList = mutableListOf<MarketPostModel>()

                        //데이터 받아오기
                        for (snapshot in querySnapshot.documents) {
                            val item = snapshot.toObject(MarketPostModel::class.java)
                            marketItemList.add(item!!)
                        }

                        //어답터에 데이터 넘겨줌
                        adapter.setData(marketItemList)
                    }
            }
        }


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


    }




    //공동구매 게시글 데이터를 가져오기 위한 메소드
    private fun getMarketData() {
//         retrieve data for the RecyclerView
//         This method should return a list of YourData objects
        db.collection("Market")   //작업할 컬렉션
            //게시일 최근순 기준으로 정렬
            .orderBy("postDate", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener

                val marketItemList = mutableListOf<MarketPostModel>()

                //데이터 받아오기
                for (snapshot in querySnapshot.documents) {
                    val item = snapshot.toObject(MarketPostModel::class.java)
                    marketItemList.add(item!!)
                }

                //어답터에 데이터 넘겨줌
                adapter.setData(marketItemList)
            }
    }

    //리사이클러뷰 어댑터
    class MarketAdapter : RecyclerView.Adapter<MarketViewHolder>() {
        private lateinit var binding: ActivityMarketitemBinding
        private val data = arrayListOf<MarketPostModel>()

        fun setData(newData: List<MarketPostModel>) {
            data.clear()
            data.addAll(newData)
            notifyDataSetChanged()
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            binding = ActivityMarketitemBinding.inflate(inflater, parent, false)
            return MarketViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
            val item = data[position]
            holder.bind(item)

        }

        override fun getItemCount(): Int = data.size


    }

    //리사이클러뷰 아이템 모델에 대한 뷰홀더
    class MarketViewHolder(private val binding: ActivityMarketitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MarketPostModel) {
            // //PostModel에 담긴 postTitle, postImg 데이터를 아이템 레이아웃에 binding
            binding.postTitle.text = item.postTitle
            binding.userLocationText.text = item.userLocation
            val price = item.price?.toInt()
            binding.price.text = "${price}"


            //이미지가 있는 글이라면
            if (item.postImageUrl != null) {
                //이미지뷰와 실제 이미지 데이터를 묶음
                Glide
                    .with(binding.postImg)
                    .load(item.postImageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_logo)
                    .into(binding.postImg)
            }

            // Set click listener on the root view of the item
//            binding.root.setOnClickListener {
//                //클릭된 게시글의 id를 MarketPostActivity로 전달하고 MarketPostActivity시작
//                val context = binding.root.context
//                val intent = Intent(context, MarketPostActivity::class.java)
//                intent.putExtra("postId", item.postId)
//                context.startActivity(intent)
//
//
//            }

        }
    }

    private fun searchCommunityData(keyword: String) {
        db.collection("Market")
            .orderBy("postDate", Query.Direction.DESCENDING)
            .whereArrayContains("postTitleKeywords", keyword)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener

                val marketItemList = mutableListOf<MarketPostModel>()

                for (snapshot in querySnapshot.documents) {
                    val item = snapshot.toObject(MarketPostModel::class.java)
                    item?.postId = snapshot.id
                    marketItemList.add(item!!)
                }

                adapter.setData(marketItemList)
            }
    }


    private fun searchMarketData(keyword: String) {
        db.collection("Market")
            .orderBy("postDate", Query.Direction.DESCENDING)
            .whereEqualTo("postTitle", keyword) // 검색어와 게시물의 제목이 일치하는 게시물만 가져오도록 쿼리 조건 추가
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener

                val marketItemList = mutableListOf<MarketPostModel>()

                for (snapshot in querySnapshot.documents) {
                    val item = snapshot.toObject(MarketPostModel::class.java)
                    item?.postId = snapshot.id // 게시물의 ID를 가져옴
                    marketItemList.add(item!!)
                }

                adapter.setData(marketItemList)
            }
    }
}

    // 공동구매 게시글 데이터 모델
    data class MarketPostModel(
        var postId: String = "",
        var postImageUrl: String? = null,
        var postTitle: String? = null,
        var userLocation: String? = null,
        var price: Long? = null
    )
