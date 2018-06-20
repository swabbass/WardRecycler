package com.wardabbass.redit.ui.fragments

import android.content.Context
import android.os.Bundle
import com.wardabbass.redit.common.BaseFragment
import com.wardabbass.redit.common.filters.BasicFilter
import com.wardabbass.redit.common.filters.RedditContainsFilter
import com.wardabbass.redit.ui.delegate.RedditPostClickDelegate

abstract class FilteredFragment : BaseFragment() {

    protected var redditPostClickDelegate: RedditPostClickDelegate? = null
    protected lateinit var filter: RedditContainsFilter
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        redditPostClickDelegate = context as RedditPostClickDelegate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filter = RedditContainsFilter()
    }

    override fun onDetach() {
        super.onDetach()
        redditPostClickDelegate = null
    }

    /**
     *     we dont need to handle saving the state since its handled by the hosed activity
     *     and this will be called when its available
     *     form activity when saved instance is available
     */
    var query = ""
        set(value) {
            field = value // set the value and call on change
            filter.query=value
            onQueryChanged()
        }


    /**
     * called internally whe query for filtering changed
     * use query property to access the modified query
     * and implement the logic here
     */
    protected abstract fun onQueryChanged()


}