package com.wardabbass.redit.ui.delegate

import com.wardabbass.redit.models.ReditPost

interface RedditPostBookMarkDelegate {
    fun onReditPostBokkmarked(post:ReditPost,bookMarked:Boolean)
}