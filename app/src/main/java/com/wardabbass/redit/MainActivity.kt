package com.wardabbass.redit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewCompat
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.wardabbass.redit.models.ReditPost
import com.wardabbass.redit.ui.delegate.RedditPostBookMarkDelegate
import com.wardabbass.redit.ui.delegate.RedditPostClickDelegate
import com.wardabbass.redit.ui.fragments.BookmarkedFragment
import com.wardabbass.redit.ui.fragments.FilteredFragment
import com.wardabbass.redit.ui.fragments.TopFeedFragment
import com.wardabbass.redit.ui.fragments.WebViewFragment
import com.wardabbass.redit.ui.viewpager.NonSwipeCrossFadeViewPager
import com.wardabbass.redit.ui.viewpager.ReditFragmentAdapter
import com.wardabbass.redit.viewmodels.MainActivityViewModel
import org.jetbrains.anko.dip
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), RedditPostClickDelegate, RedditPostBookMarkDelegate {


    companion object {
        const val EXTRA_QUERY = "query"
        const val EXTRA_SELECTED_SCREEN = "screen"
        const val MIN_QUERY_FOR_SEARCH = 3
    }

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var searchMenuItem: MenuItem? = null
    private var searchView: SearchView? = null
    private lateinit var viewPager: NonSwipeCrossFadeViewPager
    private lateinit var navigationView: BottomNavigationView
    private lateinit var reditFragmentAdapter: ReditFragmentAdapter<FilteredFragment>

    private var webFragment: WebViewFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setTranslationZ(findViewById(R.id.fragmentContainer), dip(17f).toFloat())
        initToolbar()
        initViewModel()
        initViewPager()
        initBottomNavigation()
        checkDataFromSavedInstance(savedInstanceState)

    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    /**
     * initialize view model to survive configuration changes
     */
    private fun initViewModel() {
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.query.observe(this, Observer<String> { value ->
            viewPager.post {
                // post when the viewpager already finished laying the fragments
                for (i in 0 until reditFragmentAdapter.count) {
                    reditFragmentAdapter.getCachedItem(i).query = value ?: ""
                }
            }


        })
    }

    private fun checkDataFromSavedInstance(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mainActivityViewModel.query.value = savedInstanceState.getString(EXTRA_QUERY, "")
            mainActivityViewModel.selectedScreen = savedInstanceState.getInt(EXTRA_SELECTED_SCREEN, 0)
            viewPager.setCurrentItem(mainActivityViewModel.selectedScreen, false)
        }
    }

    private fun initBottomNavigation() {
        navigationView = findViewById(R.id.navigationView)
        navigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.feed -> {
                    viewPager.setCurrentItem(0, false)
                    mainActivityViewModel.selectedScreen = 0
                    true
                }
                R.id.fav -> {
                    viewPager.setCurrentItem(1, false)
                    mainActivityViewModel.selectedScreen = 1
                    true
                }
                else -> false
            }
        }
    }

    private fun initViewPager() {
        viewPager = findViewById(R.id.viewPager)
        reditFragmentAdapter = ReditFragmentAdapter(supportFragmentManager, arrayOf(TopFeedFragment(), BookmarkedFragment()))
        viewPager.adapter = reditFragmentAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        searchMenuItem = menu.findItem(R.id.action_search)
        searchView = searchMenuItem?.actionView as SearchView
        //when activity restored with saved query
        if (searchMenuItem?.isActionViewExpanded == false && mainActivityViewModel.query.value?.isNotBlank() == true) {
            searchMenuItem?.expandActionView()
            searchView?.setQuery(mainActivityViewModel.query.value, false)
        }
        initSearchCallbacks()
        return true
    }

    /**
     * inits the callbacks for search view , expand/collapse or query submit
     */
    private fun initSearchCallbacks() {
        searchMenuItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                handleSearchMenuExpanded()
                return true

            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                handleSearchMenuCollapsed()
                return true
            }

        })
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                handleQuerySubmitted(query)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
    }

    /**
     * handle query when changed
     */
    private fun handleQuerySubmitted(query: String) {
        //toast("query $query")
        if (query.length >= MIN_QUERY_FOR_SEARCH)
            mainActivityViewModel.query.value = query
        else
            longToast(getString(R.string.min_search_query))
    }

    private fun handleSearchMenuCollapsed() {
        // toast("menu collapsed")
        searchView?.setQuery("", false)
        mainActivityViewModel.query.value = ""
    }

    private fun handleSearchMenuExpanded() {
        // toast("menu expanded")

        //   clearSearchMenuItem?.isVisible = false

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(EXTRA_QUERY, mainActivityViewModel.query.value)
        outState?.putInt(EXTRA_SELECTED_SCREEN, mainActivityViewModel.selectedScreen)

    }

    override fun onReditPostBokkmarked(post: ReditPost, bookMarked: Boolean) {
        //  toast("${post.title} is bookmarked? $bookMarked")
        val bookMarksFrag = reditFragmentAdapter.getCachedItem(1) as BookmarkedFragment
        if (bookMarked) {
            bookMarksFrag.makeBookmark(post)
        } else {
            bookMarksFrag.makeUnBookmark(post)
        }
    }

    override fun onRedditPostClicked(post: ReditPost) {
        val bookMarksFrag = reditFragmentAdapter.getCachedItem(1) as BookmarkedFragment

        webFragment = WebViewFragment.newInstance(post, bookMarksFrag.isBookmarked(post.id))

        supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.push_left_in,
                        R.anim.push_left_out,
                        R.anim.push_right_in,
                        R.anim.push_right_out
                )
                .add(R.id.fragmentContainer, webFragment, WebViewFragment.TAG)
                .addToBackStack(null)
                .commit()
    }

    override fun onBackPressed() {
        if (webFragment?.onBackPressed() == true) {
            return
        }
        super.onBackPressed()
    }


}
