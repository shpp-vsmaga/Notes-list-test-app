package com.noteslist.app.screens.notes.ui

import androidx.appcompat.app.AlertDialog
import com.noteslist.app.R
import com.noteslist.app.common.di.viewModel
import com.noteslist.app.common.ui.BaseToolbarFragment
import com.noteslist.app.common.utils.navigateTo
import com.noteslist.app.databinding.FragmentNotesBinding
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesScreenFragment : BaseToolbarFragment<NotesScreenVM, FragmentNotesBinding>() {
    override val viewModel: NotesScreenVM by viewModel()

    override fun layoutResId() = R.layout.fragment_notes

    override fun initFragment() {
        initMenu()
        viewModel.notesEvent.observe(viewLifecycleOwner, {
            handleNotesEvent(it)
        })
    }

    private fun handleNotesEvent(event: NotesScreenVM.Companion.NotesActions?) {
        when (event) {
            NotesScreenVM.Companion.NotesActions.LOGOUT -> openStartScreen()
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
}