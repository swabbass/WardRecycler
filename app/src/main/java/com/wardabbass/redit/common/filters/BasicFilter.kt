package com.wardabbass.redit.common.filters

interface BasicFilter<T> {
    fun check(t:T):Boolean
}