package com.surround2023.surround2023.market_log

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.surround2023.surround2023.R
import com.surround2023.surround2023.databinding.MarketLogOpenItemBinding
import com.surround2023.surround2023.market_log.MarketLogOpenItem

// 커스텀 뷰홀더
class MarketLogOpenItemViewHolder(private val binding: MarketLogOpenItemBinding):RecyclerView.ViewHolder(binding.root) {

    val TAG:String="로그"

    private val titleTextView=binding.productTitle
    private val productImageView=binding.productImg
    private val timeTextView=binding.timeText
    private val locationTextView=binding.locationText

    //default constructor
    init {
        Log.d(TAG, "RecordViewHolder - init() called")
    }

    //데이터와 뷰를 묶음
    fun bind(item: MarketLogOpenItem){
        Log.d(TAG, "RecordViewHolder - init() called")

        //텍스트뷰와 실제 텍스트 데이터를 묶음
        titleTextView.text=item.title
        timeTextView.text=item.time
        locationTextView.text=item.location


        //이미지뷰와 실제 이미디지 데이터를 묶음
        Glide
            .with(productImageView)
            .load(item.productImg)
//            .centerCrop()
            .placeholder(R.drawable.strawberry)
            .into(productImageView)

    }



}