package com.wardabbass.redit.ui.viewpager

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class NonSwipeCrossFadeViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {
    var swipeEnabled = false
    var enableTouch = true


    init {
        setPageTransformer(false, { view, position ->
            if (position <= -1.0F || position >= 1.0F) {        // [-Infinity,-1) OR (1,+Infinity]
                view.setAlpha(0.0F);
                view.setVisibility(View.GONE);
            } else if( position == 0.0F ) {     // [0]
                view.setAlpha(1.0F);
                view.setVisibility(View.VISIBLE);
            } else {

                // Position is between [-1,1]
                view.setAlpha(1.0F - Math.abs(position));
              //  view.setTranslationX(-position * (view.getWidth() / 2));
                view.setVisibility(View.VISIBLE);
            }

        })
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            if (swipeEnabled) {
                return super.onInterceptTouchEvent(ev)
            }
        } catch (e: Exception) {

        }

        return false
    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        try {
            if (!enableTouch) {
                return true
            }
            if (swipeEnabled) {
                return super.onTouchEvent(ev)
            }
        } catch (e: Exception) {

        }

        return false
    }
}
