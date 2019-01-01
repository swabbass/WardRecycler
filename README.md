
# WardRecycler

[![](https://jitpack.io/v/swabbass/wardRecycler.svg)](https://jitpack.io/#swabbass/wardRecycler) 

[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)

[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-WardRecycler-green.svg?style=flat )]( https://android-arsenal.com/details/1/7333 )

A wrapper for recycler view with pull to refresh and load more functionality.

This library is written in 100% kotlin.

![](2mz4n6.gif)

## install

Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. Add the dependency

```
	dependencies {
	        implementation 'com.github.swabbass:wardRecycler:{see JitPack version above}'
	}
```

## Basic and Simple

### from xml:
```
<com.ward.wrecycler.WardRecycler
    android:layout_width="match_parent"
    android:id="@+id/pullToLoadView"
    android:layout_height="match_parent"/>
```

### from code you can initialize using proper constructor :

```
WardRecycler(context)

Or

WardRecycler(context,attr)

Or

WardRecycler(context,attr,theme)
```

### set whatever adapter you want and any subclass of LinearlayoutManager like GridLayoutManager :

```
wardRecycler.setAdapter(adapter)

wardRecycler.layoutManager=LinearLayoutManager(context) or GridLayoutManager(context,spanCount)
```

### Callbacks using function passing ^_^ :

```
        wardRecycler.onRefresh = {
            handleRefresh()
        }
       
        wardRecycler.onLoadMore = { currentItemCount, pageSize ->
            handleLoadMore()
        }
        
        //to make auto refresh 
        wardRecycler.initLoading()
```

### Refreshing and load more functionality:

```
//To disable pull to refresh
wardRecycler.disableRefresh()

//To enable pull to refresh (enabled by default)
wardRecycler.enableRefresh()

//When detecting that there is no more data to load
wardRecycler.isLastPage = !haveMoreToLoad

//to finish loading from refresh or load more when the load is finished
wardRecycler.completeLoading()

//to enable load more by default it is enabled
wardRecycler.enableLoadMore=true

//items per page default is 25 used for detecting load more efficiently
wardRecycler.pageSize=DEFAULT_PAGE_SIZE
```

### Customization:

```

//you can override dimen to change progress size 
R.dimen.recycler_view_progress_size

//progress bar size dp (default R.dimen.recycler_view_progress_size ) now used for spacing you can change size by overriding the resourse
wardRecycler.progressBarSize=dip(10)

// item spacing (left and right top bottom) default 0
wardRecycler.itemSpacing=dip(10)

//progress color (use actual color ## NOT the resource)
wardRecycler.progressColor=Color.RED

//color scheme of refresh use colors not resourcers default is theme primary,accent...
wardRecycler.setColorSchemeResources(
                color1,
                color2,
                color3);
```
