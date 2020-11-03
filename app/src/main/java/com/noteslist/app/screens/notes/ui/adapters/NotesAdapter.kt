package com.noteslist.app.screens.notes.ui.adapters

import android.view.ViewGroup
import com.noteslist.app.common.ui.BaseAdapter
import com.noteslist.app.common.ui.BaseViewHolder
import com.noteslist.app.notes.models.view.Note

class NotesAdapter(
    private val clickListener: (Note) -> Unit
) :
    BaseAdapter<Note>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Note> =
        NoteViewHolder(parent, clickListener)
}