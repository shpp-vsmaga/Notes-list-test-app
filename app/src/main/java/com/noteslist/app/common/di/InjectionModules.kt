package com.noteslist.app.common.di

import com.noteslist.app.auth.di.AuthInjectionModule
import com.noteslist.app.notes.di.NotesInjectionModule
import com.noteslist.app.screens.auth.di.AuthScreenInjectionModule
import com.noteslist.app.screens.main.di.MainActivityInjectionModule
import com.noteslist.app.screens.note.di.NoteScreenInjectionModule
import com.noteslist.app.screens.notes.di.NotesScreenInjectionModule
import org.kodein.di.Kodein

object InjectionModules {

    val appDependencyInjectionModule = Kodein.Module("Main injection module") {
        import(CommonInjectionModule.module)
        import(AuthInjectionModule.module)
        import(MainActivityInjectionModule.module)
        import(AuthScreenInjectionModule.module)
        import(NotesScreenInjectionModule.module)
        import(NotesInjectionModule.module)
        import(NoteScreenInjectionModule.module)
    }
}