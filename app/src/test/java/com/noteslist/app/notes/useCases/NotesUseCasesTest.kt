package com.noteslist.app.notes.useCases

import com.noteslist.app.common.network.ConnectivityHelper
import com.noteslist.app.helpers.TestData
import com.noteslist.app.notes.gateway.NotesLocalGateway
import com.noteslist.app.notes.gateway.NotesRemoteGateway
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class NotesUseCasesTest : TestCase() {
    @Mock
    private lateinit var localGateway: NotesLocalGateway

    @Mock
    private lateinit var remoteGateway: NotesRemoteGateway

    @Mock
    private lateinit var connectivityHelper: ConnectivityHelper


    private lateinit var notesUseCases: NotesUseCases

    public override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        notesUseCases = NotesUseCases(remoteGateway, localGateway, connectivityHelper)
    }

    //should return list of notes with valid data
    @Test
    fun testGetNotesSuccess() {
        Mockito.`when`(localGateway.getNotes())
            .thenReturn(flowOf(TestData.getNotesUiModels(TestData.notesValidData)))
        runBlocking {
            notesUseCases.getNotes().collect {
                assertEquals(TestData.getNotesUiModels(TestData.notesValidData), it)
            }
        }
    }

    fun testGetNotesError() {
        Mockito.`when`(localGateway.getNotes())
            .thenReturn(callbackFlow { close(Exception("Test exception")) })
        runBlocking {
            try {
                notesUseCases.getNotes().collect()
            } catch (e: Exception) {
                assertNotNull(e)
            }
        }
    }

    //should complete with success with valid data
    @Test
    fun testFetchNotes() {
        Mockito.`when`(remoteGateway.getNotes())
            .thenReturn(flowOf(TestData.getNotesUiModels(TestData.notesValidData)))
        runBlocking {
            notesUseCases.fetchNotes()
        }
    }
}