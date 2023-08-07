package com.surround2023.surround2023.set_location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.surround2023.surround2023.R
import com.surround2023.surround2023.databinding.ActivitySearchAddressBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchAddressActivity:AppCompatActivity() {
    var TAG:String="로그"

    private lateinit var binding: ActivitySearchAddressBinding

    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 332e0c6513db0104e361758ea011774a"  // REST API 키
    }

    private val listItems = arrayListOf<SearchAddressItem>() // 리사이클러 뷰 아이템
    private val listAdapter = RecyclerAdapter(listItems) // 리사이클러 뷰 어댑터
    var selectedTown:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchAddressBinding.inflate(layoutInflater)
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

        Log.d(TAG, "paint activity_search_address.xml")

        //setLocation의 검색창에서 받아온 query를 SearchAddressActivity의 검색창에도 뿌림
        var searchView=binding.searchView
        val intent = intent
        val searchQuery = intent.getStringExtra("SEARCH_QUERY")
        if (!searchQuery.isNullOrEmpty()) {
            //query를 넘겨주고 자동으로 search가 submit(true)
            searchView.setQuery(searchQuery, true)
            searchAddress(searchQuery)
        }

        // ------------------ 주소 검색 이벤트 리스너
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 사용자가 검색 버튼을 눌렀을 때 호출되는 콜백
                if (!query.isNullOrEmpty()) {
                    // 검색어가 null 또는 빈 문자열이 아니라면 검색어를 사용하여 주소 검색
                    searchAddress(query)
                }

                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 사용자가 검색어를 입력할 때마다 호출되는 콜백
                if (!newText.isNullOrEmpty()) {
                    searchAddress(newText)
                    binding.noResultText.visibility = View.GONE // Hide noResultText when there is input
                } else {

                }
                return true
            }
        })


        //----------뒤로가기 버튼 이벤트
        binding.prevPageBtn.setOnClickListener{
            if(searchView.hasFocus()){
                //검색창이 활성화 되어 있을 땐 검색창을 초기화하고
                searchView.setQuery("", false)
                //키보드를 숨김
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchView.windowToken, 0)

                searchView.clearFocus()
                Log.d(TAG, "searchView Focus : ${searchView.hasFocus()}")

            }
            else{
                // 검색창이 비활성화 되어 있을시엔
                // SearchAddressActivity를 종료하여 이전 화면으로 돌아감
                Log.d(TAG, "SearchAddressActivity is finished")
                finish()
            }

        }


        //리사이클러뷰
        binding.searchResultRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.searchResultRecyclerView.adapter=listAdapter

        // 리사이클러뷰 아이템 클릭 이벤트
        listAdapter.setOnItemClickListener(object : RecyclerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //region_3depth가 null이라면 region_3depth_h를 할당
                selectedTown = listItems[position].region_3depth ?: listItems[position].region_3depth_h
                // 리사이클러뷰에서 선택된 아이템을 SetLocationActivity에 전달
                val intent = Intent(this@SearchAddressActivity, SetLocationActivity::class.java)
                intent.putExtra("SEARCHED_ADDRESS", selectedTown)
                setResult(10, intent)
                finish()

            }
        })

    }

    //주소 검색 함수
    private fun searchAddress(address: String){
        val retrofit = Retrofit.Builder()   // Retrofit 구성
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoAPI::class.java)   // 통신 인터페이스를 객체로 생성
        val call = api.getSearcAddress(API_KEY, address)   // 검색 조건 입력

        //API서버에 요청
        call.enqueue(object: Callback<ResultSearchKeyword> {
            override fun onResponse(
                call: Call<ResultSearchKeyword>,
                response: Response<ResultSearchKeyword>
            ) {
                // 통신 성공 (과는 response.body()에 담겨있음)
                Log.d("TAG", "Raw: ${response.raw()}")
                Log.d("TAG", "Body: ${response.body()}")
                addItems(response.body())
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // 통신 실패
                Log.w("TAG", "통신 실패: ${t.message}")
            }
        })
    }

    // 검색 결과 처리 함수
    private fun addItems(searchResult: ResultSearchKeyword?){
        if(!searchResult?.documents.isNullOrEmpty()){
            listItems.clear()   //리스트 초기화
            for (document in searchResult!!.documents){
                // 결과를 리사이클러 뷰의 아이템으로 추가
                val item= SearchAddressItem(document.address.address_name,document.address.region_3depth_name?: "",document.address.region_3depth_h_name?: "")
                Log.d("TAG","${item}")
                listItems.add(item)
            }
            listAdapter.notifyDataSetChanged()
        } else {
            binding.noResultText.visibility = View.VISIBLE
        }
    }

    class RecyclerAdapter(val itemList:ArrayList<SearchAddressItem>):RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.search_address_result_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        //----------아이템 클릭 이벤트를 위한 코드
        interface OnItemClickListener {
            fun onItemClick(position: Int)
        }

        private var onItemClickListener: OnItemClickListener? = null

        fun setOnItemClickListener(listener: OnItemClickListener) {
            onItemClickListener = listener
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.address.text=itemList[position].address
            // 아이템 클릭 이벤트
            holder.itemView.setOnClickListener {
                onItemClickListener?.onItemClick(position)
            }
        }

        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val address: TextView = itemView.findViewById(R.id.addressText)
        }



    }



}




//리사이클러뷰 아이템 클래스
class SearchAddressItem(
    val address:String,  //지번 주소
    val region_3depth:String?,   ////지역 3 Depth, 동 단위
    val region_3depth_h:String?  //지역 3 Depth,  행정동 명칭
)

