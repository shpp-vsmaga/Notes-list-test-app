package com.noteslist.app.screens.notes.ui

import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.noteslist.app.R
import com.noteslist.app.common.di.viewModel
import com.noteslist.app.common.ui.BaseToolbarFragment
import com.noteslist.app.common.utils.navigateTo
import com.noteslist.app.databinding.FragmentNotesBinding
import com.noteslist.app.notes.models.Note
import com.noteslist.app.screens.Arguments
import com.noteslist.app.screens.notes.ui.adapters.NotesAdapter
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesScreenFragment : BaseToolbarFragment<NotesScreenVM, FragmentNotesBinding>() {
    override val viewModel: NotesScreenVMImpl by viewModel()

    override fun layoutResId() = R.layout.fragment_notes
    private lateinit var adapter: NotesAdapter

    override fun initFragment() {
        initMenu()
        initNotesListAdapter()
        viewModel.notesAction.observe(viewLifecycleOwner, {
            handleNotesEvent(it)
        })
        viewModel.openEditNoteAction.observe(viewLifecycleOwner, {
            openNoteScreen(it)
        })
        viewModel.notesListData.observe(viewLifecycleOwner, {
            showNotesList(it)
        })
    }

    private fun showNotesList(notes: List<Note>?) {
        notes?.let {
            adapter.setItems(it)
        }
    }


    private fun initNotesListAdapter() {
        val clickListener: (Note) -> Unit = { link ->
            handleNoteClick(link)
        }
        adapter = NotesAdapter(clickListener)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_notes.setHasFixedSize(true)
        rv_notes.layoutManager = layoutManager
        rv_notes.adapter = adapter
    }

    private fun handleNoteClick(note: Note) {
        viewModel.editNote(note)
    }

    private fun handleNotesEvent(event: NotesScreenVM.Companion.NotesScreenAction?) {
        when (event) {
            NotesScreenVM.Companion.NotesScreenAction.LOGOUT -> openStartScreen()
            NotesScreenVM.Companion.NotesScreenAction.ADD_NOTE -> openNoteScreen()
        }
    }

    private fun openStartScreen() {
        getNavController()?.navigateTo(R.id.action_notesScreenFragment_to_authScreenFragment)
    }

    private fun initMenu() {
        toolbarDelegate?.inflateMenu(R.menu.menu_notes)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_logout -> {
                    showLogoutDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.are_you_sure_want_to_logout))
            .setMessage(getString(R.string.logout_message))
            .setNegativeButton(getString(android.R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.logout)) { _, _ ->
                viewModel.logout()
            }
            .show()
    }

    private fun openNoteScreen(note: Note? = null) {
        val bundle = bundleOf(
            Arguments.ARGUMENT_NOTE to note
        )
        getNavController()?.navigateTo(
            R.id.action_notesScreenFragment_to_noteScreenFragment,
            bundle
        )
    }
}