package com.wardabbass.redit.viewmodels

import android.arch.lifecycle.ViewModel
import com.wardabbass.redit.models.ReditPost

class BookmarkedViewModel : ViewModel() {

    private var bookMarkedIds = HashSet<String>()
    val bookMarkedRedditPosts = mutableListOf<ReditPost>()
    fun alreadyBookmarked(id: String): Boolean {
        return bookMarkedIds.contains(id)
    }

    fun bookMark(id: String) {
        bookMarkedIds.add(id)
    }
    fun unBookmark(id:String){
        bookMarkedIds.remove(id)
    }


}