package com.oblivion.personaljournal.di

import android.content.Context
import com.oblivion.personaljournal.data.JournalDao
import com.oblivion.personaljournal.data.JournalDatabase
import com.oblivion.personaljournal.data.JournalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): JournalDatabase = JournalDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideJournalDao(database: JournalDatabase): JournalDao = database.journalDao()

    @Provides
    @Singleton
    fun provideJournalRepository(journalDao: JournalDao): JournalRepository = JournalRepository(journalDao)
}
