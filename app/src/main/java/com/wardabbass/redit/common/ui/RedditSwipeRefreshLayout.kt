package com.wardabbass.redit.common.ui

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet

/**
 * custom refresh layout which will call custom callback
 * when there is a check for child that can scroll
 */
class RedditSwipeRefreshLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : SwipeRefreshLayout(context, attributeSet) {

    var canScrollChildDelegate: () -> (Boolean) = { false }
    override fun canChildScrollUp(): Boolean {

        return canScrollChildDelegate()
    }
}