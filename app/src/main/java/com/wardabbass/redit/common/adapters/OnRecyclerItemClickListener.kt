package com.wardabbass.flickergallery.common.adapters

import android.view.View

interface BaseRecyclerItemClickListener

interface OnRecyclerItemClickListener<D> : BaseRecyclerItemClickListener {

    fun onItemClicked(item:D, position:Int,view: View)
}