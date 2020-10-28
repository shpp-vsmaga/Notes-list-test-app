package com.noteslist.app.screens.auth.di

import com.noteslist.app.common.di.InjectionModule
import com.noteslist.app.screens.auth.ui.AuthScreenVM
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

object AuthScreenInjectionModule : InjectionModule {
    override val module = Kodein.Module(this.javaClass.name) {
        bind<AuthScreenVM>() with provider {
            AuthScreenVM()
        }
    }
}