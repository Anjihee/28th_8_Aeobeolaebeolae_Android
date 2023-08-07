package com.surround2023.surround2023.home

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import com.surround2023.surround2023.R

class EditTextAsSearchBar: AppCompatEditText {
    private var drawableRightClickListener: OnDrawableRightClickListener? = null
    private var touchX: Float = 0f
    private var touchY: Float = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    interface OnDrawableRightClickListener {
        fun onDrawableRightClick(view: EditTextAsSearchBar, text: String?)
    }

    fun setOnDrawableRightClickListener(listener: OnDrawableRightClickListener) {
        drawableRightClickListener = listener
    }

    fun endSearch() {
        // Clear the text in the EditText
        setText("")

        // Hide the soft keyboard
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)

        // Clear focus from the EditText to disable the search action
        clearFocus()

        // Set the custom drawable as the right drawable
        val customDrawable: Drawable? = context.getDrawable(R.drawable.ic_search)
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, customDrawable, null)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            //when the view is touched
            touchX = event.x
            touchY = event.y

            // Set the custom drawable as the right drawable
            val customDrawable: Drawable? = context.getDrawable(R.drawable.baseline_clear_24)
            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, customDrawable, null)
        }
        if (event.action == MotionEvent.ACTION_UP) {
            val drawableRight: Drawable? = compoundDrawablesRelative[2]
            if (drawableRight != null) {
                val bounds: Rect = drawableRight.bounds
                val xClick: Float = touchX - (width - paddingRight - bounds.width())
                val yClick: Float = touchY - paddingTop
                if (xClick >= 0 && xClick <= bounds.width() && yClick >= 0 && yClick <= height - paddingBottom) {
                    // Check if the text is not null
                    if (!text.isNullOrEmpty()) {
                        Log.d("Search","set the text as null")
                        setText("")
                        Log.d("Search","${text.isNullOrEmpty()}")

                    } else {
                        // No text in the EditText, end the search
                        Log.d("Search","endSearch() is called")
                        endSearch()

                    }
                    // Call the click listener callback with the current text
                    drawableRightClickListener?.onDrawableRightClick(this, text?.toString())
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }



}