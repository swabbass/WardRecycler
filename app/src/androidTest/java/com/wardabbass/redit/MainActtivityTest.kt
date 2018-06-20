package com.wardabbass.redit

import android.support.design.widget.BottomNavigationView
import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v4.view.ViewPager
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActtivityTest {
    @get:Rule
    public val mainActivityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun tabsTest() {
        Espresso.onView(withId(R.id.feed)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.fav)).check(matches(isDisplayed()))
        val bottomNavigationView = mainActivityTestRule.activity.findViewById<BottomNavigationView>(R.id.navigationView)
        val viewPager = mainActivityTestRule.activity.findViewById<ViewPager>(R.id.viewPager)
        assertTrue(bottomNavigationView.menu.findItem(R.id.feed).isChecked)
        assertTrue(viewPager.currentItem == 0)

    }
}