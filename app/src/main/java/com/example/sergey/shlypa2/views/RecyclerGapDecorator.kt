package com.example.sergey.shlypa2.views

import android.graphics.Rect
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.extensions.dpToPx

class RecyclerGapDecorator : RecyclerView.ItemDecoration() {

  private var dividedPairs = listOf<Pair<Int, Int>>()
  private var gapsBeforeFirst = mutableMapOf<Int, Int>()
  private var gapSize = 0

  private var viewTypes: Set<Int>? = null

  private var leftOffset = 0
  private var rightOffset = 0
  private var topOffset = 0
  private var bottomOffset = 0

  fun withExtraOffsetBetween(gapDp: Int, vararg pairs: Pair<Int, Int>): RecyclerGapDecorator {
    gapSize = gapDp.dpToPx
    dividedPairs = pairs.toList()
    return this
  }

  fun withViewTypes(vararg layouts: Int): RecyclerGapDecorator {
    viewTypes = layouts.toHashSet()
    return this
  }

  fun withExtraOffsetIfFirst(@LayoutRes layout: Int, gapDp: Int): RecyclerGapDecorator {
    gapsBeforeFirst[layout] = gapDp.dpToPx
    return this
  }

  fun withOffset(left: Int, right: Int, top: Int, bottom: Int): RecyclerGapDecorator {
    leftOffset = left.dpToPx
    rightOffset = right.dpToPx
    topOffset = top.dpToPx
    bottomOffset = bottom.dpToPx
    return this
  }

  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: RecyclerView.State
  ) {
    val position = parent.getChildAdapterPosition(view)

    parent.adapter?.let {
      val holderOneLayout = it.getItemViewType(position)
      val holderTwoLayout = it.getItemViewType(position + 1)

      if(position == 0) {
        gapsBeforeFirst[holderOneLayout]?.let {
          outRect.top = it
        }
      }

      if (dividedPairs.contains(holderOneLayout to holderTwoLayout)) {
        outRect.bottom += gapSize
      }

      if(viewTypes == null || viewTypes?.contains(holderOneLayout) == true) {
        outRect.left = leftOffset
        outRect.right = rightOffset
        outRect.top += topOffset
        outRect.bottom += bottomOffset
      }
    }
  }
}