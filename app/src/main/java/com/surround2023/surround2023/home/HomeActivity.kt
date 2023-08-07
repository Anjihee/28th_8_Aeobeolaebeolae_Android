package com.surround2023.surround2023.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.surround2023.surround2023.R
import com.surround2023.surround2023.community.CommunityholderActivity
import com.surround2023.surround2023.community_post.CommunityPostActivity
import com.surround2023.surround2023.databinding.ActivityHomeBinding
import com.surround2023.surround2023.databinding.HomeCommunityItemBinding
import com.surround2023.surround2023.databinding.HomeMarketItemBinding
import com.surround2023.surround2023.market.MarketholderActivity
import com.surround2023.surround2023.market_post.MarketPostActivity
import com.surround2023.surround2023.mypage.MypageActivity
import com.surround2023.surround2023.posting.MarketPostingActivity
import com.surround2023.surround2023.set_location.SetLocationActivity
import com.surround2023.surround2023.user_login_join.UserSingleton
import java.security.MessageDigest



class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    //하단 Nav 와 관련된 변수
    private lateinit var bottomNavView: BottomNavigationView

    //리사이클러뷰
    private lateinit var marketRecyclerView: RecyclerView
    private lateinit var communityRecyclerView: RecyclerView
    private lateinit var adapterForMarket: HomeMarketAdapter
    private lateinit var adapterForCommunity: HomeCommunityAdapter


    //설정된 사용자 위치를 가져오기 위한 상수
    private val LOCATION_REQUEST_CODE = 1001

    //게시글 데이터베이스
    val db=FirebaseFirestore.getInstance()  //Firestore 인스턴스 선언
    val storage= Firebase.storage

    //유저 데이터
//    private lateinit var userData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 소프트키(네비게이션 바), 상태바를 숨기기 위한 플래그 설정
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)


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

        //        ---------------------사용자 위치 설정 기능 -------------------------------
        binding.userLocationArea.setOnClickListener{
            showDropdownMenu(it)
        }

        //공동구매 전체보기 -> 공동구매 게시판 액티비티로 이동
        binding.goMarket.setOnClickListener {
            val intent = Intent(this, MarketholderActivity::class.java)
            startActivity(intent)
        }

        //공동구매 전체보기 -> 커뮤니티 게시판 액티비티로 이동
        binding.goCommunity.setOnClickListener {
            val intent = Intent(this, CommunityholderActivity::class.java)
            startActivity(intent)
        }


        //-------------------------------공구/커뮤니티 리사이클러뷰-------------------------
        // Initialize RecyclerView
        marketRecyclerView= binding.marketRecycler
        communityRecyclerView=binding.communityRecycler

        //커뮤니티 리사이클러뷰 레이아웃매니저
        val communityLayoutManager = LinearLayoutManager(this)
        communityRecyclerView.layoutManager = communityLayoutManager


        // Initialize Adapter
        adapterForMarket = HomeMarketAdapter()
        adapterForCommunity = HomeCommunityAdapter()
        marketRecyclerView.adapter = adapterForMarket
        communityRecyclerView.adapter=adapterForCommunity

        // Add item decoration to the marketRecyclerView
        val itemSpace = resources.getDimensionPixelSize(R.dimen.home_market_item_space) // 이 부분에서 R.dimen.item_space는 간격 크기를 리소스로 정의한 값
        marketRecyclerView.addItemDecoration(HomeMarketItemDecoration(itemSpace, 2))

        //공동구매 리사이클러뷰 레이아웃매니저
        val marketLayoutManager=GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false)
        marketRecyclerView.layoutManager=marketLayoutManager

        //populate data
        getMarketData()
        getCommunityData()




        //-------------------------- 검색창 핸들 ---------------------------
        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Get the search text from the EditText
                val searchText = binding.searchBar.text.toString().trim()

                // Call the searchMarketPost method to perform the search
                searchMarketPost(searchText)

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        //
        setUserLocationData()

    }




    //검색 동작 처리를 위한 메소드
    private fun searchMarketPost(searchText:String){
        val intent = Intent(this, MarketholderActivity::class.java)
        //MarketPostActivity로 검색 텍스트 searchText를 전달
        intent.putExtra("searchText", searchText)
        startActivity(intent)

    }

    //사용자 위치 Dropdown 메뉴를 보여주는 함수
    private fun showDropdownMenu(view:View){

        val popupMenu = PopupMenu(this,view)
        popupMenu.menuInflater.inflate(R.menu.location_dropdown_menu,popupMenu.menu) //menu layout

        // Dropdown 메뉴의 아이템 클릭 이벤트 처리
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.setUserLocation -> {
                    goSetLocationPage()
                    true
                }

                else -> false
            }
        }
        //Dropdown 메뉴가 보여질 위치 조절
//        popupMenu.gravity=Gravity.START
        popupMenu.show()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setUserLocation -> {
                // 하단 소프트키 숨기기
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

                return true
            }
            // 다른 메뉴 항목에 대한 처리
            else -> return super.onOptionsItemSelected(item)
        }
    }

    //사용자 위치 설정 페이지로 이동하기 위한 메소드
    fun goSetLocationPage(){
        val intent = Intent(this, SetLocationActivity::class.java)
        startActivityForResult(intent, LOCATION_REQUEST_CODE)// 위치 설정 액티비티 호출
    }

    //사용자 위치 설정 페이지에서 설정된 위치를 home에도 적용

    // 위치 설정 액티비티로부터 결과를 받아오는 콜백 메소드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val currentStreetAddress = data?.getStringExtra("CURRENT_ADDRESS")
            if (!currentStreetAddress.isNullOrEmpty()) {
                // SetLocationActivity로부터 전달받은 주소를 화면에 업데이트
                binding.userLocationText.text = currentStreetAddress
                setUserLocationData()
            }
        }
    }

    //공동구매 게시글 데이터를 가져오기 위한 메소드
    private fun getMarketData(){
        // retrieve data for the RecyclerView
        // This method should return a list of YourData objects
        db.collection("Market")   //작업할 컬렉션
            //게시일 최근순 기준으로 정렬
            .orderBy("postDate", Query.Direction.DESCENDING)
            .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                if(querySnapshot==null) return@addSnapshotListener

                val marketItemList = mutableListOf<MarketPostModel>()

                //데이터 받아오기
                for(snapshot in querySnapshot.documents){
                    val item=snapshot.toObject(MarketPostModel::class.java)
                    Log.d("TAG","${item?.postId},${item?.postImageUrl}, ${item?.postTitle}")
                    marketItemList.add(item!!)
                }

                //어답터에 데이터 넘겨줌
                adapterForMarket.setData(marketItemList)
            }
//        return listOf(
//            //데이터를 담을 배열
//            //Set DummyData
//            MarketPostModel("@drawable/strawberry","딸기 1키로 공구하실 분 구합니다"),
//
//            // Add more items as needed
//        )

    }

    //커뮤니티 게시글 데이터를 가져오기 위한 메소드
    private fun getCommunityData(){
        // Replace with your own implementation to retrieve data for the RecyclerView
        // This method should return a list of YourData objects
        db.collection("Community")   //작업할 컬렉션
            .orderBy("postDate", Query.Direction.DESCENDING)
            .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                if(querySnapshot==null) return@addSnapshotListener

                val communityItemList = mutableListOf<CommunityPostModel>()

                //데이터 받아오기
                for(snapshot in querySnapshot.documents){
                    var item=snapshot.toObject(CommunityPostModel::class.java)
                    communityItemList.add(item!!)
                }


                adapterForCommunity.setData(communityItemList)
            }

    }

    //공동구매 리사이클러뷰 어댑터
    class HomeMarketAdapter: RecyclerView.Adapter<HomeMarketViewHolder>(){
        private lateinit var binding: HomeMarketItemBinding
        private val data = arrayListOf<MarketPostModel>()

        fun setData(newData: List<MarketPostModel>) {
            data.clear()
            data.addAll(newData)
            notifyDataSetChanged()
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMarketViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            binding = HomeMarketItemBinding.inflate(inflater, parent, false)
            return HomeMarketViewHolder(binding)
        }

        override fun onBindViewHolder(holder: HomeMarketViewHolder, position: Int) {
            val item=data[position]
            holder.bind(item)

        }

        override fun getItemCount(): Int = data.size


    }

    // 공동구매 리사이클러뷰 아이템 모델에 대한 뷰홀더
    class HomeMarketViewHolder(private val binding: HomeMarketItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MarketPostModel) {
            // Bind data to the views in your item layout
            binding.productTitle.text=item.postTitle


            //이미지가 있는 글이라면
            if (item.postImageUrl != null) {
                //이미지뷰와 실제 이미지 데이터를 묶음
                Glide
                    .with(binding.productImg)
                    .load(item.postImageUrl)
                    .centerCrop()
                    .placeholder(R.color.colorLightGrey)
                    .into(binding.productImg)
            }   else {
                //이미지가 없는 글이라면
                //이미지 바인딩을 하지 않음
            }

            // Set click listener on the root view of the item
            binding.root.setOnClickListener {
                //클릭된 게시글의 id를 MarketPostActivity로 전달하고 MarketPostActivity시작
                val context = binding.root.context
                val intent = Intent(context, MarketPostActivity::class.java)
                intent.putExtra("postId", item.postId)
                context.startActivity(intent)

            }
        }
    }



    //커뮤니티 리사이클러뷰 어댑터
    class HomeCommunityAdapter(): RecyclerView.Adapter<HomeCommunityViewHolder>(){
        private val data = mutableListOf<CommunityPostModel>()

        fun setData(newData: List<CommunityPostModel>) {
            data.clear()
            data.addAll(newData)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCommunityViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HomeCommunityItemBinding.inflate(inflater, parent, false)
            return HomeCommunityViewHolder(binding)
        }

        override fun onBindViewHolder(holder: HomeCommunityViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount(): Int = data.size
    }

    // 커뮤니티 리사이클러뷰 아이템 모델에 대한 뷰홀더
    class HomeCommunityViewHolder(private val binding: HomeCommunityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommunityPostModel) {
            // //PostModel에 담긴 postTitle, postImg 데이터를 아이템 레이아웃에 binding
            binding.postTitle.text=item.postTitle


            //이미지가 있는 글이라면
            if (item.postImageUrl != null) {
                //이미지뷰와 실제 이미지 데이터를 묶음
                Glide
                    .with(binding.postImg)
                    .load(item.postImageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_logo)
                    .into(binding.postImg)
            }   else {
                //이미지가 없는 글이라면
                //이미지 숨김
                binding.postImgContainer.visibility=View.GONE
                binding.postImg.visibility=View.GONE

                // TextView의 constraint 재설정
                val layoutParams = binding.postTitle.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                binding.postTitle.layoutParams = layoutParams

            }

            // Set click listener on the root view of the item
            binding.root.setOnClickListener {
                //클릭된 게시글의 id를 CommunityPostActivity로 전달하고 CommunityPostActivity시작
                val context = binding.root.context
                val intent = Intent(context, CommunityPostActivity::class.java)
                intent.putExtra("postId", item.postId)
                context.startActivity(intent)


            }

        }
    }

    //유저 데이터에 location을 설정하기 위한 메서드
    fun setUserLocationData(){
        val userSingleton=UserSingleton.getInstance()
        val userData=userSingleton.getUserData()
        val currentUserLocation=binding.userLocationText.text.toString()       //home에서 설정된 currentLocation을 받아옴

        if (userData!=null){
            userData.userLocation=currentUserLocation
            Log.d("UserData","${userData}")
        }

        //변경된 userData 를 db에 update
        val userCollection=db.collection("User")
        userCollection.whereEqualTo("email",userData!!.Email)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents){
                    // Update the document with the new userData
                    userCollection.document(doc.id).set(userData)
                        .addOnSuccessListener { Log.d("UserData", "${userData}") }
                        .addOnFailureListener { e ->
                            Log.w("UserData", "Error updating document", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w("UserData", "Error getting documents: ", e)
            }
    }


}


// 공동구매 게시글 데이터 모델
data class MarketPostModel(
    var postId:String="",
    var postImageUrl: String?=null,
    var postTitle:String?=null)



// 커뮤니티 게시글 데이터 모델
data class CommunityPostModel(
    var postId:String="",
    var postImageUrl:String?=null,
    var postTitle:String?=null
)

