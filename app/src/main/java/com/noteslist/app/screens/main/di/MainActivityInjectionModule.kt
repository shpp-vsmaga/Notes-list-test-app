package com.noteslist.app.screens.main.di

import com.noteslist.app.common.di.InjectionModule
import com.noteslist.app.screens.main.ui.MainActivityVM
import com.noteslist.app.screens.main.ui.MainActivityVMImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

/**
 * Separate injection module, contains bindings for instances injected in MainActivity package, like
 * Room DAO, repositories implementations, view models, etc.
 */
object MainActivityInjectionModule : InjectionModule {
    override val module = Kodein.Module(this.javaClass.name) {
        bind<MainActivityVM>() with provider {
            MainActivityVMImpl(instance())
        }
    }
}