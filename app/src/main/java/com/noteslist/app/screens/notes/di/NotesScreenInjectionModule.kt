package com.noteslist.app.screens.notes.di

import com.noteslist.app.common.di.InjectionModule
import com.noteslist.app.screens.auth.ui.AuthScreenVM
import com.noteslist.app.screens.main.ui.MainActivityVM
import com.noteslist.app.screens.notes.ui.NotesScreenVM
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

object NotesScreenInjectionModule : InjectionModule {
    override val module = Kodein.Module(this.javaClass.name) {
        bind<NotesScreenVM>() with provider {
            NotesScreenVM(instance())
        }
    }
}