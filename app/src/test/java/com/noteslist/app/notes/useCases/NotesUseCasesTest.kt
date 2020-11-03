package com.noteslist.app.notes.useCases

import android.util.Log
import com.noteslist.app.common.network.ConnectivityHelper
import com.noteslist.app.helpers.TestData
import com.noteslist.app.notes.gateway.NotesLocalGateway
import com.noteslist.app.notes.gateway.NotesRemoteGateway
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import junit.framework.TestCase
import org.joda.time.DateTimeZone
import org.joda.time.tz.UTCProvider
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

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
        //required for the correct instantiation of DateTime class
        DateTimeZone.setProvider(UTCProvider())

        MockitoAnnotations.initMocks(this)
        notesUseCases = NotesUseCases(remoteGateway, localGateway, connectivityHelper)
    }

    //should return list of notes with valid data
    fun testGetNotesSuccess() {
        Mockito.`when`(localGateway.getNotes())
            .thenReturn(Flowable.just(TestData.getNotesUiModels(TestData.notesValidData)))

        val testObserver = notesUseCases.getNotes().test()
        testObserver.assertValue(TestData.getNotesUiModels(TestData.notesValidData))
    }

    //should return error with wrong data
    fun testGetNotesError() {
        Mockito.`when`(localGateway.getNotes())
            .thenReturn(Flowable.unsafeCreate {
                try {
                    it.onNext(TestData.getNotesUiModels(TestData.notesWrongData))
                } catch (e: Exception){
                    it.onError(e)
                }
            })

        notesUseCases.getNotes()
            .subscribe({
                assertNull(it)
            },{
                assertNotNull(it)
            })
    }

    //should complete with success with valid data
    fun testFetchNotes() {
        Mockito.`when`(remoteGateway.getNotes())
            .thenReturn(Single.just(TestData.getNotesUiModels(TestData.notesValidData)))
        Mockito.`when`(localGateway.saveNotes(TestData.getNotesUiModels(TestData.notesValidData)))
            .thenReturn(Completable.complete())

        val testObserver = notesUseCases.fetchNotes().test()
        testObserver.assertComplete()
    }
}