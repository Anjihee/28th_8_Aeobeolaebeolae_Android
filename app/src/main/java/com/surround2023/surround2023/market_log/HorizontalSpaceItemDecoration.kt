package com.surround2023.surround2023.market_log

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class HorizontalItemDecoration(private val horizontalSpaceWidth: Int, private val firstItemLeftSpace: Int): RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)


        val position = parent.getChildAdapterPosition(view)

        if (position == 0) {
            outRect.left = firstItemLeftSpace
            outRect.right = horizontalSpaceWidth
        } else if (position != parent.adapter?.itemCount?.minus(1)) {
            //마지막 아이템을 제외한 모든 아이템에 오른쪽 여백을 설정
            outRect.right = horizontalSpaceWidth
        }
    }

    // view사이즈에 변화를 주거나 여백을 설정할 땐 dp -> px 변환 필요
    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}