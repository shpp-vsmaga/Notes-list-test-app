package com.noteslist.app.screens.note.di

import com.noteslist.app.common.di.InjectionModule
import com.noteslist.app.screens.note.ui.NoteScreenVM
import com.noteslist.app.screens.note.ui.NoteScreenVMImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

object NoteScreenInjectionModule : InjectionModule {
    override val module = Kodein.Module(this.javaClass.name) {
        bind<NoteScreenVM>() with provider {
            NoteScreenVMImpl(instance())
        }
    }
}