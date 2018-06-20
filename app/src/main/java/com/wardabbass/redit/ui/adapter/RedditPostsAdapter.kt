package com.wardabbass.redit.ui.adapter

import android.view.ViewGroup
import com.wardabbass.flickergallery.common.adapters.BaseAdapter
import com.wardabbass.redit.models.ReditPost
import com.wardabbass.redit.ui.reddit.ReditPostView

class RedditPostsAdapter : BaseAdapter<ReditPost, ReditPostClickListener, RedditPostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditPostViewHolder {

        var reditPostView = ReditPostView(parent.context)
        return RedditPostViewHolder(reditPostView)
    }
}