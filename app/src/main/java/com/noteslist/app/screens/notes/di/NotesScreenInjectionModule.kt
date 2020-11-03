package com.noteslist.app.screens.notes.di

import com.noteslist.app.common.di.InjectionModule
import com.noteslist.app.screens.notes.ui.NotesScreenVM
import com.noteslist.app.screens.notes.ui.NotesScreenVMImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

/**
 * Separate injection module, contains bindings for instances injected in Notes list package, like
 * Room DAO, repositories implementations, view models, etc.
 */
object NotesScreenInjectionModule : InjectionModule {
    override val module = Kodein.Module(this.javaClass.name) {
        bind<NotesScreenVM>() with provider {
            NotesScreenVMImpl(instance(), instance())
        }
    }
}