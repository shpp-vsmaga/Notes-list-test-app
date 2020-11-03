package com.noteslist.app.screens.auth.di

import com.noteslist.app.common.di.InjectionModule
import com.noteslist.app.screens.auth.ui.AuthScreenVM
import com.noteslist.app.screens.auth.ui.AuthScreenVMImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

/**
 * Separate injection module, contains bindings for instances injected auth screen package, like
 * Room DAO, repositories implementations, view models, etc.
 */
object AuthScreenInjectionModule : InjectionModule {
    override val module = Kodein.Module(this.javaClass.name) {
        bind<AuthScreenVM>() with provider {
            AuthScreenVMImpl()
        }
    }
}