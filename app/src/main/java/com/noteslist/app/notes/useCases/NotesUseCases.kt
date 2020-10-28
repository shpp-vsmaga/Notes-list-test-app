package com.noteslist.app.notes.useCases

import com.noteslist.app.notes.gateway.NotesLocalGateway
import com.noteslist.app.notes.gateway.NotesRemoteGateway

class NotesUseCases(
    private val notesRemoteGateway: NotesRemoteGateway,
    private val notesLocalGateway: NotesLocalGateway
) {
}