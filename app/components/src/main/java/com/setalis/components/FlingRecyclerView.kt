package com.setalis.components

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * Crafted by Al (ismealdi) on 31/01/24.
 * Setalis Digital
 */
class FlingRecyclerView : RecyclerView {
    var screenWidth: Int

    constructor(context: Context) : super(context) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        val linearLayoutManager = layoutManager as LinearLayoutManager?
        //these four variables identify the views you see on screen.
        val lastVisibleView = linearLayoutManager!!.findLastVisibleItemPosition()
        val firstVisibleView = linearLayoutManager.findFirstVisibleItemPosition()
        val firstView = linearLayoutManager.findViewByPosition(firstVisibleView)
        val lastView = linearLayoutManager.findViewByPosition(lastVisibleView)
        //these variables get the distance you need to scroll in order to center your views.
//my views have variable sizes, so I need to calculate side margins separately.
//note the subtle difference in how right and left margins are calculated, as well as
//the resulting scroll distances.
        val leftMargin = (screenWidth - lastView!!.width) / 2
        val rightMargin = (screenWidth - firstView!!.width) / 2 + firstView.width
        val leftEdge = lastView.left
        val rightEdge = firstView.right
        val scrollDistanceLeft = leftEdge - leftMargin
        val scrollDistanceRight = rightMargin - rightEdge
        //if(user swipes to the left)
        if (velocityX > 0) smoothScrollBy(
            scrollDistanceLeft,
            0
        ) else smoothScrollBy(-scrollDistanceRight, 0)
        return true
    }
}