package com.surround2023.surround2023.community_log

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityLogViewPager2FragmentAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = mutableListOf<Fragment>()
    private val fragmentTitleList = mutableListOf<String>()

    // itemList을 받아서 각각의 Fragment에 전달하는 함수를 추가합니다.
    fun submitList(itemList: ArrayList<MyPostModel>) {
        fragmentList.clear()
        fragmentTitleList.clear()

        // 각 Fragment에 itemList을 전달합니다.
        val fragment1 = CommunityLogPostsFragment.newInstance(itemList)
//        val fragment2 = CommunityLogCommentsFragment.newInstance(itemList)

        fragmentList.add(fragment1)
//        fragmentList.add(fragment2)

        fragmentTitleList.add("게시글")
        fragmentTitleList.add("댓글")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}