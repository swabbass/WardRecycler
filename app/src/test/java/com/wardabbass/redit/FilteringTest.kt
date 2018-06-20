package com.wardabbass.redit

import com.wardabbass.redit.common.filters.RedditContainsFilter
import com.wardabbass.redit.models.ReditPost
import org.fluttercode.datafactory.impl.DataFactory
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class FilteringTest {

    lateinit var posts: MutableList<ReditPost>
    lateinit var strings: MutableList<String>
    lateinit var dataFactory: DataFactory
    lateinit var filter: RedditContainsFilter
    lateinit var magicWord: String
    lateinit var random: Random
    @Before
    fun setup() {
        filter = RedditContainsFilter()
        posts = mutableListOf()
        strings = mutableListOf()
        dataFactory = DataFactory()
        magicWord = dataFactory.randomWord
        random = Random()
        generateStrings()
        generatePosts()
    }

    @Test
    fun testBasicFiltering() {
        filter.query = strings[0]
        Assert.assertEquals(filter.query, strings[0])
        val results = posts.filter { filter(it) }
        Assert.assertNotEquals(results.size, 0)
        assert(results[0].title.contains(strings[0], true))
        assert(results[0].title.contains(strings[0], false))
        Assert.assertNotEquals(results[0].title.contains(strings[0].toUpperCase(), false), true)
        Assert.assertNotEquals(results[0].title.contains(strings[0].toUpperCase(), true), false)
    }

    @Test
    fun testSubStringFiltering() {
        filter.query = strings[0].substring(5, 10)
        val results = posts.filter { filter(it) }
        Assert.assertNotEquals(results.size, 0)
        assert(results[0].title.contains(filter.query, true))
        assert(results[0].title.contains(filter.query, false))
        Assert.assertNotEquals(results[0].title.contains(filter.query.toUpperCase(), false), true)
        Assert.assertNotEquals(results[0].title.contains(filter.query.toUpperCase(), true), false)
    }

    @Test
    fun testFilteringResult() {
        filter.query = strings[0].substring(5, 10)
        var results = posts.filter { filter(it) }
        Assert.assertEquals(2, results.size)
        generateStrings(true)
        generatePosts()
        filter.query = magicWord
        results = posts.filter { filter(it) }
        Assert.assertEquals(10, results.size)
        val multiResults = mutableListOf<ReditPost>()
        var factor = 0
        for (i in 0 until strings.size step 10) {
            if (i + factor >= strings.size) {
                continue
            }
            filter.query = strings[i + factor]
            results = posts.filter { filter(it) }
            Assert.assertEquals(2, results.size)
            multiResults.addAll(results)
            factor += 1
        }
        filter.query = magicWord
        results = posts.filter { filter(it) }

        Assert.assertEquals(results.size*2,multiResults.size)

    }

    @After
    fun destroy(){
        strings.clear()
        posts.clear()
        filter.query=""
        magicWord=""

    }

    private fun generateStrings(suffle: Boolean = false) {
        strings.clear()
        for (i in 0 until 100) {
            val element = dataFactory.getRandomText(20, 140)
            strings.add(element)
            if (i % 10 == 0) {
                if (suffle) {
                    //if true then every 10th item we will have 2 item including 0 ,0+1,10+11,20+21....
                    var element1: String
                    when (random.nextInt(2)) {
                        0 -> element1 = magicWord + element
                    /* 1 -> {
                         val index = random.nextInt(15)
                         element1 = element.substring(0, index) + magicWord + element.substring(index, element.length - 1)
                     }*/
                        1 -> element1 = element + magicWord
                        else -> {
                            element1 = magicWord + element
                        }
                    }
                    strings.add(element1)
                } else {
                    strings.add(element)
                }
            }

        }
    }

    private fun generatePosts() {

        posts.clear()
        for (string in strings) {
            posts.add(ReditPost(title = string, id = dataFactory.getRandomChars(10)))
        }
    }
}