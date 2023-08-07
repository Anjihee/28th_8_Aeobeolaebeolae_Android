package com.surround2023.surround2023.community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.surround2023.surround2023.R
import com.surround2023.surround2023.category.BuyCategoryActivity
import com.surround2023.surround2023.community_post.CommunityPostActivity
import com.surround2023.surround2023.databinding.ActivityCommunityholderBinding
import com.surround2023.surround2023.databinding.ActivityCommunityitemBinding
import com.surround2023.surround2023.home.HomeActivity
import com.surround2023.surround2023.mypage.MypageActivity
import com.surround2023.surround2023.posting.CommunityPostingActivity
import com.surround2023.surround2023.posting.MarketPostingActivity


class CommunityholderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityholderBinding

    //하단 Nav 와 관련된 변수
    private lateinit var bottomNavView: BottomNavigationView

    //리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CommunityAdapter

    //게시글 데이터베이스
    val db= FirebaseFirestore.getInstance()  //Firestore 인스턴스 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityholderBinding.inflate(layoutInflater)
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


        // Initialize RecyclerView
        recyclerView=binding.recyclerView

        //리사이클러뷰 레이아웃매니저
        val recyclerLayoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager=recyclerLayoutManager

        // Initialize Adapter
        adapter= CommunityAdapter()
        recyclerView.adapter=adapter

        // item 사이 밑줄 추가
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, recyclerLayoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)


        //populate data
        getCommunityData()


    }

    //커뮤니티 게시글 데이터를 가져오기 위한 메소드
    private fun getCommunityData(){
//         retrieve data for the RecyclerView
//         This method should return a list of YourData objects
        db.collection("Community")   //작업할 컬렉션
            //게시일 최근순 기준으로 정렬
            .orderBy("postDate", Query.Direction.DESCENDING)
            .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                if(querySnapshot==null) return@addSnapshotListener

                val communityItemList = mutableListOf<CommunityPostModel>()

                //데이터 받아오기
                for(snapshot in querySnapshot.documents){
                    val item=snapshot.toObject(CommunityPostModel::class.java)
                    communityItemList.add(item!!)
                }

                //어답터에 데이터 넘겨줌
                adapter.setData(communityItemList)
            }
    }



    //커뮤니티 리사이클러뷰 어댑터
    class CommunityAdapter(): RecyclerView.Adapter<CommunityViewHolder>(){
        private val data = mutableListOf<CommunityPostModel>()

        fun setData(newData: List<CommunityPostModel>) {
            data.clear()
            data.addAll(newData)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ActivityCommunityitemBinding.inflate(inflater, parent, false)
            return CommunityViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount(): Int = data.size
    }


    // 커뮤니티 리사이클러뷰 아이템 모델에 대한 뷰홀더
    class CommunityViewHolder(private val binding: ActivityCommunityitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommunityPostModel) {
            // //PostModel에 담긴 postTitle, postImg 데이터를 아이템 레이아웃에 binding
            binding.postTitle.text=item.postTitle
            binding.category.text=item.category
            binding.likeNum.text=item.likeNum.toString()
            binding.commentsNum.text=item.commentsNum.toString()
            binding.postContent.text=item.postContent

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
                binding.postImgContainer.visibility= View.GONE
                binding.postImg.visibility= View.GONE

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

}

// 커뮤니티 게시글 데이터 모델
data class CommunityPostModel(
    var postId:String="",
    var category:String?=null,
    var postContent:String?=null,
    var postImageUrl:String?=null,
    var postTitle:String?=null,
    var likeNum:Int?=null,
    var commentsNum:Int?=null
)