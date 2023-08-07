package com.surround2023.surround2023.market

/* import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.surround2023.surround2023.databinding.ItemMarketBinding
import com.surround2023.surround2023.market.Marketmemo

class Marketadapter : RecyclerView.Adapter<Marketadapter.MarketViewHolder>() {

    private var marketList: List<Marketmemo> = emptyList()

    fun setData(markets: List<Marketmemo>) {
        marketList = markets
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val binding = ItemMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.bind(marketList[position])
    }

    override fun getItemCount(): Int = marketList.size

    class MarketViewHolder(private val binding: ItemMarketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(market: Marketmemo) {
            binding.ivImage.setImageResource(market.image)
            binding.tvPostTitle.text = market.postTitle
            binding.tvPostContent.text = market.postContent
        }
    }
} */