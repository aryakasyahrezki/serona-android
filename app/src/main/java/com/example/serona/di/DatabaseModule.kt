package com.example.serona.di

import android.content.Context
import androidx.room.Room
import com.example.serona.data.local.room.SeronaDatabase
import com.example.serona.data.local.room.dao.UserDao
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
    fun provideDatabase(@ApplicationContext context: Context): SeronaDatabase {
        return Room.databaseBuilder(
            context,
            SeronaDatabase::class.java,
            "serona_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: SeronaDatabase): UserDao = database.userDao()
}