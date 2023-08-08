package com.surround2023.surround2023.community_log

import android.os.Bundle
import android.os.Parcelable
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.surround2023.surround2023.R
import com.surround2023.surround2023.databinding.FragmentCommunityLogPostsBinding
import com.surround2023.surround2023.databinding.CommunityLogPostsItemBinding
import com.surround2023.surround2023.home.MarketPostModel
import com.surround2023.surround2023.user_login_join.UserSingleton
import kotlinx.android.parcel.Parcelize


class CommunityLogPostsFragment: Fragment() {

    //뷰바인딩
    private lateinit var binding: FragmentCommunityLogPostsBinding
    //리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyPostAdapter

    companion object {
        private const val ARG_ITEM_LIST = "item_list"

        // itemList을 전달하는 새로운 Fragment 인스턴스를 생성하는 팩토리 메서드를 만듭니다.
        fun newInstance(itemList: ArrayList<MyPostModel>): CommunityLogPostsFragment {
            val fragment = CommunityLogPostsFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_ITEM_LIST, itemList as ArrayList<out Parcelable>?)
            fragment.arguments = args
            return fragment
        }
    }


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


        // Initialize RecyclerView and Adapter
        recyclerView = binding.recyclerView
        adapter=MyPostAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Get data from itemList and set it to the adapter
        arguments?.getParcelableArrayList<MyPostModel>(ARG_ITEM_LIST)?.let { itemList ->
            adapter.setData(itemList)
        }


        recyclerView.adapter=adapter


        // item 사이 밑줄 추가
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, LinearLayoutManager(requireContext()).orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)




    }

    fun getData():List<MyPostModel>{

        return listOf(
            MyPostModel("123",null,"우웩",null,null,null,"userId"),
            MyPostModel("123",null,"우웩",null,null,null,"userId")
        )

    }

    class MyPostAdapter : RecyclerView.Adapter<MyPostAdapter.MyPostViewHolder>() {
        private val data = mutableListOf<MyPostModel>()

        fun setData(newData: ArrayList<MyPostModel>) {
            data.clear()
            data.addAll(newData)
            notifyDataSetChanged()
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

        // 리사이클러뷰 아이템 모델에 대한 뷰홀더
        class MyPostViewHolder(private val binding: CommunityLogPostsItemBinding,private val adapter: MyPostAdapter) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(item: MyPostModel) {
                // Bind data to the views in your item layout
                binding.postTitle.text=item.postTitle
                binding.postDate.text=item.postDate
                binding.viewdNum.text=item.likeNum.toString()
                binding.commentsNum.text=item.commentsNum.toString()

                //이미지가 있는 글이라면
                if (item.postImageUrl != null) {
                    //이미지뷰와 실제 이미지 데이터를 묶음
                    Glide
                        .with(binding.postImage)
                        .load(item.postImageUrl)
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



}

// 게시글 데이터 모델
@Parcelize
data class MyPostModel(
    val postId: String? = "",
    var postImageUrl: String? = null,
    var postTitle: String? = null,
    var postDate: String? = null,
    var likeNum: Int? = null,
    var commentsNum: Int? = null,
    var uid: String? = ""
) : Parcelable


