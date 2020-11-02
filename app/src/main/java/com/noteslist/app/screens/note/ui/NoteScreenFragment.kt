package com.noteslist.app.screens.note.ui

import android.graphics.drawable.Animatable
import android.os.Bundle
import com.noteslist.app.R
import com.noteslist.app.common.di.viewModel
import com.noteslist.app.common.ui.BaseToolbarFragment
import com.noteslist.app.common.utils.showMessage
import com.noteslist.app.common.utils.threadUnsafeLazy
import com.noteslist.app.databinding.FragmentNoteBinding
import com.noteslist.app.notes.models.Note
import com.noteslist.app.screens.Arguments
import kotlinx.android.synthetic.main.fragment_note.*

class NoteScreenFragment : BaseToolbarFragment<NoteScreenVM, FragmentNoteBinding>() {
    override val viewModel: NoteScreenVMImpl by viewModel()

    override fun layoutResId() = R.layout.fragment_note

    private val note by threadUnsafeLazy {
        arguments?.getParcelable<Note>(Arguments.ARGUMENT_NOTE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note?.let {
            viewModel.setNote(it)
        }
    }

    override fun initFragment() {
        viewModel.noteScreenAction.observe(viewLifecycleOwner, {
            handleNoteScreenAction(it)
        })
    }

    private fun handleNoteScreenAction(action: NoteScreenVM.Companion.NoteScreenAction?) {
        when (action) {
            NoteScreenVM.Companion.NoteScreenAction.CLOSE -> backToNotes()
            NoteScreenVM.Companion.NoteScreenAction.SHOW_ADD_MODE -> showAddMode()
            NoteScreenVM.Companion.NoteScreenAction.SHOW_EDIT_MODE -> showEditMode()
            NoteScreenVM.Companion.NoteScreenAction.SHOW_OFFLINE_MESSAGE -> showOfflineMessage()
        }
    }

    private fun showOfflineMessage() {
        showMessage(getString(R.string.offline_message))
    }

    private fun showEditMode() {
        toolbarDelegate?.setToolbarTitle(R.string.edit_note)
        initEditMenu()
    }

    private fun initEditMenu() {
        showMenu(isEdit = true)
    }

    private fun showAddMode() {
        toolbarDelegate?.setToolbarTitle(R.string.add_note)
        initAddMenu()
    }

    private fun initAddMenu() {
        showMenu(isEdit = false)
    }

    private fun backToNotes() {
        getNavController()?.navigateUp()
    }

    private fun showMenu(isEdit: Boolean) {
        toolbarDelegate?.clearMenu()
        val menuResId =
            if (isEdit) R.menu.menu_edit_note else R.menu.menu_add_note
        val menu = toolbarDelegate?.inflateMenu(menuResId)
        val progressItem = menu?.findItem(R.id.menu_progress)
        progressItem?.icon?.let { item ->
            if (item is Animatable) {
                item.start()
            }
        }

        viewModel.progressVisible.observe(viewLifecycleOwner, {
            menu?.findItem(R.id.menu_save)?.isVisible = !it
            menu?.findItem(R.id.menu_delete)?.isVisible = !it
            menu?.findItem(R.id.menu_progress)?.isVisible = it
        })

        menu?.findItem(R.id.menu_save)?.isEnabled = !isEdit && viewModel.noteTextData.isValid
        viewModel.textChangedData.observe(viewLifecycleOwner, {
            menu?.findItem(R.id.menu_save)?.isEnabled = it && viewModel.noteTextData.isValid
        })

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_save -> {
                    saveNote()
                    true
                }
                R.id.menu_delete -> {
                    deleteNote()
                    true
                }
                else -> false
            }
        }
    }

    private fun hideMenu() {
        toolbarDelegate?.clearMenu()
    }

    private fun saveNote() {
        viewModel.saveNote()
    }

    private fun deleteNote() {
        viewModel.deleteNote()
    }
}