package com.noteslist.app.screens.main.di

import com.noteslist.app.common.di.InjectionModule
import com.noteslist.app.screens.main.ui.MainActivityVM
import com.noteslist.app.screens.main.ui.MainActivityVMImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

object MainActivityInjectionModule : InjectionModule {
    override val module = Kodein.Module(this.javaClass.name) {
        bind<MainActivityVM>() with provider {
            MainActivityVMImpl(instance())
        }
    }
}