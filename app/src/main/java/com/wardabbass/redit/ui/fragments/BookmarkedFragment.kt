package com.wardabbass.redit.ui.fragments


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ward.wrecycler.WardRecycler

import com.wardabbass.redit.R
import com.wardabbass.redit.models.ReditPost
import com.wardabbass.redit.ui.adapter.RedditPostsAdapter
import com.wardabbass.redit.ui.adapter.ReditPostClickListener
import com.wardabbass.redit.viewmodels.BookmarkedViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class BookmarkedFragment : FilteredFragment() {

    companion object {
        const val TAG = "BookmarkedFragment"
    }
    val compositeDisposable = CompositeDisposable()

    private lateinit var wardRecycler: WardRecycler
    private lateinit var adapter: RedditPostsAdapter
    private lateinit var bookmarkedViewModel: BookmarkedViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_favorite, container, false)
        bookmarkedViewModel = ViewModelProviders.of(this).get(BookmarkedViewModel::class.java)
        initAdapter()
        initPullToLoadView(rootView)
        return rootView
    }

    private fun initPullToLoadView(rootView: View) {
        wardRecycler = rootView.findViewById(R.id.pullToLoadView)
        wardRecycler.setAdapter(adapter)
        wardRecycler.disableRefresh()
    }

    private fun initAdapter() {
        adapter = RedditPostsAdapter()
        adapter.items = bookmarkedViewModel.bookMarkedRedditPosts
        adapter.clickListener = object : ReditPostClickListener {
            override fun onItemClicked(item: ReditPost, position: Int, view: View) {
                redditPostClickDelegate?.onRedditPostClicked(item)
            }

        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d(TAG, " is visiable to user -> $isVisibleToUser ")
    }


    override fun onQueryChanged() {
        if (query.isNotBlank()) {
            compositeDisposable.add(  Observable.fromIterable(bookmarkedViewModel.bookMarkedRedditPosts)
                    .subscribeOn(Schedulers.computation())
                    .filter { filter.check(it) }
                    .toList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { result, error ->
                        adapter.items = result
                    })

        } else {
            adapter.items = bookmarkedViewModel.bookMarkedRedditPosts
        }
        Log.d(TAG, "onQueryChanged : $query ${context == null}")

    }



    fun isBookmarked(id: String) = bookmarkedViewModel.alreadyBookmarked(id)

    fun makeBookmark(reditPost: ReditPost) {
        if (!bookmarkedViewModel.alreadyBookmarked(reditPost.id)) {
            if (query.isNotBlank()) {
                // when search results and the bookmarked one meets the filter
                if (filter(reditPost)) {
                    adapter.add(reditPost)
                }
            } else {
                // no query the original bookmark list loaded into adapter
                adapter.add(reditPost)
            }
            //after all add it to the original list
            bookmarkedViewModel.bookMarkedRedditPosts.add(reditPost)
            bookmarkedViewModel.bookMark(reditPost.id)
        }
    }

    fun makeUnBookmark(reditPost: ReditPost) {
        if (bookmarkedViewModel.alreadyBookmarked(reditPost.id)) {
            if (query.isNotBlank()) {
                if (filter(reditPost)) {
                    // when search results and the unbookmarked one meets the filter
                    adapter.removeItem(reditPost)
                }
            } else {
                // no query the original bookmark list loaded into adapter
                adapter.removeItem(reditPost)
            }
            //after all remove it to the original list
            bookmarkedViewModel.bookMarkedRedditPosts.remove(reditPost)
            bookmarkedViewModel.unBookmark(reditPost.id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}
