package com.surround2023.surround2023.community_log

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.surround2023.surround2023.R
import com.surround2023.surround2023.databinding.FragmentCommunityLogPostsBinding
import com.surround2023.surround2023.databinding.CommunityLogPostsItemBinding


class CommunityLogPostsFragment: Fragment() {

    //뷰바인딩
    private lateinit var binding: FragmentCommunityLogPostsBinding
    //리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyPostAdapter


    override fun onCreateView(
        //뷰가 화면에 그려질 때
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentCommunityLogPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager


        // item 사이 밑줄 추가
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        // Initialize Adapter
        adapter = MyPostAdapter()
        recyclerView.adapter = adapter

        // Populate data
        val data = getData() // Replace with your data source
        adapter.setData(data)


    }

    private fun getData(): List<MyPostModel> {
        // Replace with your own implementation to retrieve data for the RecyclerView
        // This method should return a list of YourData objects
        return listOf(
            //게시글 데이터를 담을 배열
            MyPostModel("data:image/jpeg;base64,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","저희집 강아지 자랑합니다","2023.05.24",104,3),
            MyPostModel(null,"저만 월급이 스쳐 지나가요?","2023.05.24",30,5),
            MyPostModel("@drawable/cutecute_dog","저희집 강아지 자랑합니다","2023.05.24",104,3),
            MyPostModel("@drawable/cutecute_dog","저희집 강아지 자랑합니다","2023.05.24",104,3),
            MyPostModel("@drawable/cutecute_dog","저희집 강아지 자랑합니다","2023.05.24",104,3),
            MyPostModel("@drawable/cutecute_dog","저희집 강아지 자랑합니다","2023.05.24",104,3),
            MyPostModel("@drawable/cutecute_dog","저희집 강아지 자랑합니다","2023.05.24",104,3),
            MyPostModel("@drawable/cutecute_dog","저희집 강아지 자랑합니다","2023.05.24",104,3),
            // Add more items as needed
        )
    }

    // Replace YourAdapter with your own adapter implementation
    private class MyPostAdapter : RecyclerView.Adapter<MyPostViewHolder>() {
        private val data = mutableListOf<MyPostModel>()

        fun setData(newData: List<MyPostModel>) {
            data.clear()
            data.addAll(newData)
//            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = CommunityLogPostsItemBinding.inflate(inflater, parent, false)
            return MyPostViewHolder(binding,this)
        }

        override fun onBindViewHolder(holder: MyPostViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount(): Int = data.size

        fun deleteItem(position: Int){
            if (position != RecyclerView.NO_POSITION && position < data.size) {
                data.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    // 리사이클러뷰 아이템 모델에 대한 뷰홀더
    private class MyPostViewHolder(private val binding: CommunityLogPostsItemBinding,private val adapter: MyPostAdapter) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyPostModel) {
            // Bind data to the views in your item layout
            binding.postTitle.text=item.postTitle
            binding.postDate.text=item.postDate
            binding.viewdNum.text=item.viewedNum.toString()
            binding.commentsNum.text=item.commentsNum.toString()

            //이미지가 있는 글이라면
            if (item.postImg != null) {
                //이미지뷰와 실제 이미지 데이터를 묶음
                Glide
                    .with(binding.postImage)
                    .load(item.postImg)
                    .centerCrop()
                    .placeholder(R.color.colorGrey)
                    .into(binding.postImage)
            }   else {
                //이미지가 없는 글이라면
                //이미지 숨김
                binding.postImgContainer.visibility=View.GONE
                binding.postImage.visibility=View.GONE

                // postTextContainer의 constraint 재설정
                val layoutParams = binding.postTextContainer.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                binding.postTextContainer.layoutParams = layoutParams

            }

            // Set OnClickListener for deleteBtn
            binding.deleteBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Call the deleteItem method in the adapter to delete the item
                    adapter.deleteItem(position)
                }
            }

        }
        }


}

// 게시글 데이터 모델
private data class MyPostModel(
    var postImg:String?=null,
    var postTitle:String?=null,
    var postDate:String?=null,
    var viewedNum:Int?=null,
    var commentsNum:Int?=null
)


