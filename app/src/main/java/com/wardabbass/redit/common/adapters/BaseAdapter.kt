package com.wardabbass.flickergallery.common.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.wardabbass.redit.models.ReditPost


abstract class BaseAdapter<D, L : BaseRecyclerItemClickListener, VH : BaseViewHolder<D, L>> : RecyclerView.Adapter<VH>() {


    var clickListener: L? = null

    var items: MutableList<D> = mutableListOf()
        set(value) {

            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(items[position], clickListener)
    }

    /**
     * removes item from adapter if exists and notify for removal
     * @param item to remove will use equals and hashcode
     */
    fun removeItem(item: D) {
        val indexOf = items.indexOf(item)
        if (indexOf != -1) {
            items.removeAt(indexOf)
            notifyItemRemoved(indexOf)
        }

    }

    /**
     * append items and notify adapter about changes of items added only
     */
    fun addItems(items: List<D>) {
        this.items.addAll(items)
        notifyItemRangeInserted(this.items.size - items.size, items.size)
    }

    /**
     * get item by given position
     * this will show as [] operator in kotlin !
     */
    fun get(pos: Int): D = items[pos]

    /**
     * appends item to adapter and notifies the adapter
     */
    fun add(item: D) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    /**
     * clears the adapter
     */
    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
}