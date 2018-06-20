package com.wardabbass.redit.ui.fragments


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ToggleButton
import com.asksira.webviewsuite.WebViewSuite

import com.wardabbass.redit.R
import com.wardabbass.redit.common.BaseFragment
import com.wardabbass.redit.common.ui.RedditSwipeRefreshLayout
import com.wardabbass.redit.models.ReditPost
import com.wardabbass.redit.ui.delegate.RedditPostBookMarkDelegate


class WebViewFragment : BaseFragment() {


    private lateinit var titleTextView: TextView
    private lateinit var bookmarkedToggleView: ToggleButton
    private lateinit var toolbar: Toolbar
    private lateinit var webViewSuite: WebViewSuite
    private lateinit var swipeRefreshLayout: RedditSwipeRefreshLayout

    private var reditPost: ReditPost? = null
    private var bookMarked = false
    private var reditPostBookMarkDelegate: RedditPostBookMarkDelegate? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        reditPostBookMarkDelegate = context as RedditPostBookMarkDelegate
    }

    override fun onDetach() {
        super.onDetach()
        reditPostBookMarkDelegate = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extractArgs(savedInstanceState)
    }

    private fun extractArgs(savedInstanceState: Bundle?) {
        if (arguments != null) {
            reditPost = arguments?.getParcelable(EXTRA_POST) as? ReditPost
            bookMarked = arguments?.getBoolean(EXTRA_BOOKMARKED) ?: false
        }
        if (savedInstanceState != null) {
            reditPost = savedInstanceState.getParcelable(EXTRA_POST) as? ReditPost
            bookMarked = savedInstanceState.getBoolean(EXTRA_BOOKMARKED, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_web_view, container, false)
        initViews(root)
        return root;
    }

    private fun initViews(root: View) {
        toolbar = root.findViewById(R.id.toolbar)
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout)
        titleTextView = toolbar.findViewById(R.id.titleTextView)
        bookmarkedToggleView = toolbar.findViewById(R.id.toggleBookmarked)
        initWebView(root)

    }

    private fun initWebView(root: View) {

        webViewSuite = root.findViewById(R.id.webViewSuite)

        webViewSuite.customizeClient(object : WebViewSuite.WebViewSuiteCallback {
            override fun onPageFinished(view: WebView?, url: String?) {
                if (!isSafe()) {
                    return
                }

                swipeRefreshLayout.post {
                    swipeRefreshLayout.isRefreshing = false
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (!isSafe()) {
                    return
                }
                swipeRefreshLayout.post {
                    swipeRefreshLayout.isRefreshing = true
                }

            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }

        })
        swipeRefreshLayout.canScrollChildDelegate = {
            webViewSuite.webView?.scrollY?:0 > 0
        }
        swipeRefreshLayout.setOnRefreshListener {
            webViewSuite.refresh()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindBookmarkToggle()
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }
        titleTextView.text = reditPost?.title
        webViewSuite.startLoading("$REDIT_ROOT${reditPost?.permalink}")
    }

    private fun bindBookmarkToggle() {
        bookmarkedToggleView.isChecked = bookMarked
        bookmarkedToggleView.setOnClickListener {
            bookMarked = bookmarkedToggleView.isChecked
            reditPost?.let { post -> reditPostBookMarkDelegate?.onReditPostBokkmarked(post, bookMarked) }
        }
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_POST, reditPost)
    }

    override fun onBackPressed(): Boolean {
        if (webViewSuite.goBackIfPossible()) {
            return true
        }
        return super.onBackPressed()
    }

    companion object {
        const val TAG = "WebFragment"
        private const val EXTRA_POST = "post"
        private const val EXTRA_BOOKMARKED = "bookmarked"
        private const val REDIT_ROOT = "https://www.reddit.com/"
        fun newInstance(post: ReditPost, bookMarked: Boolean = false): WebViewFragment {
            val args = Bundle()
            args.putParcelable(EXTRA_POST, post)
            args.putBoolean(EXTRA_BOOKMARKED, bookMarked)
            val frag = WebViewFragment()
            frag.arguments = args
            return frag
        }
    }


}
