package com.noteslist.app.common.di

import androidx.room.Room
import com.noteslist.app.common.db.NotesDatabase
import com.noteslist.app.common.network.ConnectivityHelper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.util.concurrent.Executors

/**
 * Contains binding for common tools like DB, network, etc.
 */
object CommonInjectionModule : InjectionModule {
    private const val DATABASE_NAME = "notes_list_db"
    override val module = Kodein.Module(this.javaClass.name) {

        bind<NotesDatabase>() with singleton {
            Room
                .databaseBuilder(
                    instance(),
                    NotesDatabase::class.java,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .setQueryExecutor(Executors.newCachedThreadPool())
                .build()
        }

        bind<ConnectivityHelper>() with singleton {
            ConnectivityHelper(instance())
        }
    }
}