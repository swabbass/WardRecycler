package com.wardabbass.redit.ui.reddit

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wardabbass.redit.R
import org.jetbrains.anko.dip

class ReditPostView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, theme: Int = -1) : CardView(context, attributeSet, theme) {
    companion object {
        val HEIGHT = 100
    }

    init {
        View.inflate(context, R.layout.redit_post_view, this)
        val height = dip(HEIGHT)
        cardElevation = dip(1.2f).toFloat()
        radius = dip(4).toFloat()
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height)
    }

}