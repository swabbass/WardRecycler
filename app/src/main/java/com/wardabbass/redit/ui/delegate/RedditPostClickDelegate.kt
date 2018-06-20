package com.wardabbass.redit.ui.delegate

import com.wardabbass.redit.models.ReditPost

interface RedditPostClickDelegate {
    fun onRedditPostClicked(post:ReditPost)
}