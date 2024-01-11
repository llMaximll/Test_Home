package com.github.llmaximll.test_home

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import timber.log.Timber

@HiltAndroidApp
class TestHomeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()

        initRealm()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initRealm() {
        Realm.init(this)
    }
}