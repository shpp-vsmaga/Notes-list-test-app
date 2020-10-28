package com.noteslist.app.auth.di

import com.google.firebase.auth.FirebaseAuth
import com.noteslist.app.auth.useCases.AuthUseCases
import com.noteslist.app.common.di.InjectionModule
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object AuthInjectionModule : InjectionModule {

    override val module = Kodein.Module(this.javaClass.name) {
        bind<FirebaseAuth>() with provider { FirebaseAuth.getInstance() }
        bind<AuthUseCases>() with singleton {
            AuthUseCases(instance(), instance())
        }
    }
}