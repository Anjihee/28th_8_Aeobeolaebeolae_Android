package com.surround2023.surround2023.market

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.surround2023.surround2023.R
import com.surround2023.surround2023.databinding.ActivityMarketholderBinding
import com.surround2023.surround2023.databinding.ActivityMarketitemBinding
import com.surround2023.surround2023.market.MarketPostModel
import com.surround2023.surround2023.market_post.MarketPostActivity



class MarketholderActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMarketholderBinding

    //리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MarketAdapter

    //게시글 데이터베이스
    val db= FirebaseFirestore.getInstance()  //Firestore 인스턴스 선언

    override fun onCreate(savedInstanceState: Bundle?) {

        binding=ActivityMarketholderBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize RecyclerView
        recyclerView=binding.recyclerView

        //리사이클러뷰 레이아웃매니저
        val recyclerLayoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager=recyclerLayoutManager

        // Initialize Adapter
        adapter=MarketAdapter()
        recyclerView.adapter=adapter

        // item 사이 밑줄 추가
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, recyclerLayoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        //populate data
        getMarketData()

    }



    //공동구매 게시글 데이터를 가져오기 위한 메소드
    private fun getMarketData(){
//         retrieve data for the RecyclerView
//         This method should return a list of YourData objects
        db.collection("Market")   //작업할 컬렉션
            //게시일 최근순 기준으로 정렬
            .orderBy("postDate", Query.Direction.DESCENDING)
            .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                if(querySnapshot==null) return@addSnapshotListener

                val marketItemList = mutableListOf<MarketPostModel>()

                //데이터 받아오기
                for(snapshot in querySnapshot.documents){
                    val item=snapshot.toObject(MarketPostModel::class.java)
                    marketItemList.add(item!!)
                }

                //어답터에 데이터 넘겨줌
                adapter.setData(marketItemList)
            }
    }

    //리사이클러뷰 어댑터
    class MarketAdapter: RecyclerView.Adapter<MarketViewHolder>(){
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
            val item=data[position]
            holder.bind(item)

        }

        override fun getItemCount(): Int = data.size


    }

    //리사이클러뷰 아이템 모델에 대한 뷰홀더
    class MarketViewHolder(private val binding: ActivityMarketitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MarketPostModel) {
            // //PostModel에 담긴 postTitle, postImg 데이터를 아이템 레이아웃에 binding
            binding.postTitle.text=item.postTitle
            binding.userLocationText.text=item.userLocation
            val price=item.price?.toInt()
            binding.price.text="${price}"


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
}

// 공동구매 게시글 데이터 모델
data class MarketPostModel(
    var postId:String="",
    var postImageUrl: String?=null,
    var postTitle:String?=null,
    var userLocation:String?=null,
    var price:Long?=null
)