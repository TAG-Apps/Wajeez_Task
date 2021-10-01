package com.wajeez.sample.model.di.modules

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /*
    @Singleton
    @Provides
    fun provideDataBase(baseApp: AppApplication): AuthDatabase =
        Room.databaseBuilder(baseApp, AuthDatabase::class.java, "users_db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideDao(database: AuthDatabase): AuthDao = database.authDao()
*/



}