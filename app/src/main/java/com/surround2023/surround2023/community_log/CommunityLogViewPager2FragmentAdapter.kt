package com.surround2023.surround2023.community_log

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityLogViewPager2FragmentAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    //참고: https://curryyou.tistory.com/415

    // 1. ViewPager2에 연결할 Fragment들을 생성
    private val fragmentList = listOf(CommunityLogPostsFragment(), CommunityLogCommentsFragment())

    // 2. ViewPager2에서 노출시킬 Fragment의 개수 설정
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    // 3. ViewPager2의 각 페이지에서 노출할 Fragment 설정
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}