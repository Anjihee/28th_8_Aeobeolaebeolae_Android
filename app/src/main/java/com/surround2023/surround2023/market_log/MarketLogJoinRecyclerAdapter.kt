package com.surround2023.surround2023.market_log

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.surround2023.surround2023.databinding.MarketLogJoinItemBinding

class MarketLogJoinRecyclerAdapter:RecyclerView.Adapter<MarketLogJoinItemViewHolder>() {

    val TAG:String = "로그"

    private var modelList=ArrayList<MarketLogJoinItem>()

    //뷰홀더가 생성 되었을 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketLogJoinItemViewHolder {
        //연결할 레이아웃 xml 설정
        val binding= MarketLogJoinItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarketLogJoinItemViewHolder(binding)
    }

    //목록의 아이템 수
    override fun getItemCount(): Int {
        //필요한 아이템의 수를 리턴
        return this.modelList.size
    }

    //뷰와 뷰홀더가 묶였을 때
    override fun onBindViewHolder(holder: MarketLogJoinItemViewHolder, position: Int) {
        //아이템뷰에 가져온 데이터를 적용하는(바인딩) 메서드
        Log.d(TAG,"RecyclerAdapter - onBindViewHolder() called")
        holder.bind(this.modelList[position])
    }

    //외부에서 데이터 넘기기
    fun submitList(modelList: ArrayList<MarketLogJoinItem>){
        this.modelList=modelList
    }

    //

}