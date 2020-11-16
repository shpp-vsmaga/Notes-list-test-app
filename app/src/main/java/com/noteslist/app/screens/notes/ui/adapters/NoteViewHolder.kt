package com.noteslist.app.screens.notes.ui.adapters

import android.view.ViewGroup
import com.noteslist.app.R
import com.noteslist.app.common.ui.BaseViewHolder
import com.noteslist.app.notes.models.view.Note
import kotlinx.android.synthetic.main.item_note.view.*
import java.text.SimpleDateFormat
import java.util.*

class NoteViewHolder(
    viewGroup: ViewGroup,
    private val clickListener: (Note) -> Unit
) : BaseViewHolder<Note>(viewGroup, R.layout.item_note) {

    override fun onBind(item: Note) {
        super.onBind(item)
        itemView.setOnClickListener {
            clickListener.invoke(item)
        }
        itemView.tv_text.text = item.text
        itemView.tv_date.text = getFormattedDate(item.createdAt)
    }

    private fun getFormattedDate(date: Date): String {
        val format = SimpleDateFormat("dd MMMM, yyyy HH:mm", Locale.ENGLISH)
        return format.format(date)
    }
}