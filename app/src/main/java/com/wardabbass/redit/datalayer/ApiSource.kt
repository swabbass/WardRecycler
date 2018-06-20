package com.wardabbass.redit.datalayer

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.wardabbass.redit.common.ui.PullToLoadView.Companion.DEFAULT_PAGE_SIZE
import com.wardabbass.redit.models.RedditResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ApiSource {
    const val TAG="ApiSource"
    const val PAGE_SIZE = DEFAULT_PAGE_SIZE

    private const val ROUTE_TEMPLATE = "https://www.reddit.com/top.json?limit={limit}&after={after}"

    fun getTopFeed(limit: Int = PAGE_SIZE, after: String?=null): Single<RedditResponse> {
        AndroidNetworking.forceCancelAll() // force cancel all requests
        Log.d(TAG,"limit $limit , after $after")
        return Rx2AndroidNetworking
                .get(ROUTE_TEMPLATE)
                .addPathParameter("after", after)
                .addPathParameter("limit", "$limit")
                .build()
                .getObjectSingle(RedditResponse::class.java)
                .subscribeOn(Schedulers.io())

    }
}