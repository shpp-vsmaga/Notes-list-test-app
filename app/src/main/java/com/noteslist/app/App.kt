package com.noteslist.app

import android.app.Application
import android.content.Context
import com.noteslist.app.common.di.InjectionModules
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


class App : Application(), KodeinAware {
    override val kodein = ConfigurableKodein()

    override fun onCreate() {
        super.onCreate()
        setupKodein()
    }

    private fun setupKodein() {
        kodein.apply {
            mutable = true
            clear()
            addImport(InjectionModules.appDependencyInjectionModule)
            addImport(Kodein.Module {
                bind<Context>() with singleton { this@App }
            })
        }
    }
}