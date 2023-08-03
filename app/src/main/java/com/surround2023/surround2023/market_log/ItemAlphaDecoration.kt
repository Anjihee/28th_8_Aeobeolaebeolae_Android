package com.surround2023.surround2023.market_log

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView

class ItemAlphaDecoration (private val alpha: Float) : RecyclerView.ItemDecoration() {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val itemCount = parent.childCount
        for (i in 0 until itemCount) {
            val child = parent.getChildAt(i)
            child.alpha = alpha
            child.invalidate()
        }
    }
}