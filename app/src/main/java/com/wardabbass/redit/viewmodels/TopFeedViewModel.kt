package com.wardabbass.redit.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.wardabbass.redit.datalayer.ApiSource
import com.wardabbass.redit.models.RedditResponse
import com.wardabbass.redit.models.ReditPost
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class TopFeedViewModel : ViewModel() {

    /**
     * after param for loading more data  null for nothing to load more
     */
    var after: String? = null
    /**
     * this will have the posts all of them
     */
    var reditPosts = mutableListOf<ReditPost>()
    /**
     * filterd posts
     */
    var filterdPosts = mutableListOf<ReditPost>()


    /**
     * @param limit limit the batch from the api
     * @param loadMore if the request is for loading more data or reloading the data
     * @param onComplete callback which will be called with fromLoadMore as if the response came from loadmore
     *  haveMoreToLoad determines if we may send another load more, data as the new data
     *  @param onError callback for error passing the throwable
     */
    fun fetchFeed(limit: Int, loadMore: Boolean = false,
                  onComplete: (fromLoadMore: Boolean, haveMoreToLoad: Boolean, data: MutableList<ReditPost>) -> Unit,
                  onError: (Throwable) -> (Unit)): Disposable {
        if (!loadMore) {
            after = null
            reditPosts.clear()
            filterdPosts.clear()
        }

        return ApiSource.getTopFeed(limit, after)
                .observeOn(AndroidSchedulers.mainThread()).subscribe { result, err ->

                    err?.let {
                        //handle error
                        onError(err)
                        return@let
                    }

                    result?.let {
                        after = it.data.after
                        val newPosts = it.data.children.map { item -> item.data }.toMutableList()
                        val haveMoreToLoad = after != null
                        reditPosts.addAll(newPosts)

                        onComplete(loadMore, haveMoreToLoad, newPosts)


                    }
                }
    }


}