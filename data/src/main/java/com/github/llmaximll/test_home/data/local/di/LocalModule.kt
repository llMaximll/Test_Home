package com.github.llmaximll.test_home.data.local.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    private const val REALM_VERSION = 1L

    @Singleton
    @Provides
    fun provideRealmConfig(): RealmConfiguration =
        RealmConfiguration.Builder()
            .schemaVersion(REALM_VERSION)
            .build()
}