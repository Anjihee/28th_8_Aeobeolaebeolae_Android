package com.surround2023.surround2023.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HomeMarketItemDecoration(
    private val space: Int, // 간격 크기 (px)
    private val spanCount: Int // 열의 개수
) : RecyclerView.ItemDecoration() {

    // 아이템 간격을 설정하는 메서드
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        // 아이템의 왼쪽 간격 설정
        outRect.left = space

        // 아이템의 오른쪽 간격 설정
        outRect.right=0


        // 아이템 아래쪽 간격 설정
        outRect.bottom = space

    }
}