package com.wardabbass.redit.common

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {

    /**
     * maybe called when activity on back pressed called
     * @return false if the back click is not handled by this fragment , true otherwise
     */
    open fun onBackPressed(): Boolean {
        return false
    }


    /**
     * @return true if the fragment is not removing and not detached and attached to activity ,false otherwise
     */
    fun isSafe(): Boolean = !isRemoving && !isDetached && activity != null

}