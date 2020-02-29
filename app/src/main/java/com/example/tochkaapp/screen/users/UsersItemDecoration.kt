package com.example.tochkaapp.screen.users

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

/**
 * Created by Vladimir Kraev
 */

class UsersItemDecoration(
    private val context: Context
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val margin = calculateItemSpacing()
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            outRect.top = margin * 2
        }
        outRect.left = margin
        outRect.right = margin
        outRect.bottom = margin * 2
    }

    private fun calculateItemSpacing(): Int =
        (ITEM_SPACING_DP * context.resources.displayMetrics.density).roundToInt()


    companion object {
        private const val ITEM_SPACING_DP = 12
    }

}