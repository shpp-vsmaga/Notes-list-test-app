package com.noteslist.app.common.ui

abstract class BaseAdapter<T> : androidx.recyclerview.widget.RecyclerView.Adapter<BaseViewHolder<T>>() {

    protected val data = mutableListOf<T>()

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) = holder.onBind(data[position])

    open fun getItem(position: Int): T = data[position]

    fun isEmpty() = data.size == 0

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    open fun setItems(items: List<T>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: BaseViewHolder<T>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}