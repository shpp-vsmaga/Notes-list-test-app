package com.noteslist.app

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import com.noteslist.app.common.di.InjectionModules
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins
import net.danlew.android.joda.JodaTimeAndroid
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber


class App : Application(), KodeinAware {
    override val kodein = ConfigurableKodein()

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        setupKodein()
        setRxErrorHandler()
    }

    private fun setupKodein() {
        kodein.apply {
            mutable = true
            clear()
            addImport(InjectionModules.appDependencyInjectionModule)
            addImport(Kodein.Module {
                bind<ContentResolver>() with singleton { this@App.contentResolver }
                bind<Context>() with singleton { this@App }
            })
        }
    }

    private fun setRxErrorHandler() {
        val oldHandler = RxJavaPlugins.getErrorHandler()
        RxJavaPlugins.setErrorHandler { throwable ->
            when (throwable) {
                is UndeliverableException -> Timber.d("subscription was cancelled")
                is InterruptedException -> Timber.d("some blocking code was interrupted by a dispose call")
                else -> acceptRxThrowable(oldHandler, throwable)
            }
        }
    }

    private fun acceptRxThrowable(handler: Consumer<in Throwable>?, throwable: Throwable) {
        if (handler != null) {
            handler.accept(throwable)
        } else {
            Thread.currentThread()
                .uncaughtExceptionHandler
                ?.uncaughtException(Thread.currentThread(), throwable)
        }
    }
}