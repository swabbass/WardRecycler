package com.wardabbass.redit.common.filters

import com.wardabbass.redit.models.ReditPost

class RedditContainsFilter(var query:String="") : BasicFilter<ReditPost>{
    override fun check(t: ReditPost): Boolean {
        return t.title.contains(query,true)
    }

    operator fun invoke(t:ReditPost):Boolean=check(t)

}