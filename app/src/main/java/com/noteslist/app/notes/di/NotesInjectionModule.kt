package com.noteslist.app.notes.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.noteslist.app.common.db.NotesDatabase
import com.noteslist.app.common.di.InjectionModule
import com.noteslist.app.notes.db.NotesDao
import com.noteslist.app.notes.gateway.NotesDatabaseGateway
import com.noteslist.app.notes.gateway.NotesFirebaseGateway
import com.noteslist.app.notes.gateway.NotesLocalGateway
import com.noteslist.app.notes.gateway.NotesRemoteGateway
import com.noteslist.app.notes.useCases.NotesUseCases
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object NotesInjectionModule : InjectionModule {
    override val module = Kodein.Module(this.javaClass.name) {
        bind<NotesDao>() with singleton { instance<NotesDatabase>().notesDao() }
        bind<NotesLocalGateway>() with provider {
            NotesDatabaseGateway(instance())
        }
        bind<FirebaseFirestore>() with provider { Firebase.firestore }
        bind<NotesRemoteGateway>() with provider {
            NotesFirebaseGateway(instance(), instance())
        }
        bind<NotesUseCases>() with provider {
            NotesUseCases(instance(), instance())
        }

    }
}