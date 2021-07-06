package com.annevonwolffen.todoapp.di

import android.content.Context
import androidx.room.Room
import com.annevonwolffen.data.database.DB_NAME
import com.annevonwolffen.data.database.TasksDatabase
import com.annevonwolffen.todoapp.notification.NotificationHelper
import com.annevonwolffen.todoapp.utils.CoroutineDispatchers
import com.annevonwolffen.todoapp.utils.CoroutineDispatchersImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface AppModule {

    @AppScope
    @Binds
    fun bindCoroutinesDispatcher(coroutineDispatchers: CoroutineDispatchersImpl): CoroutineDispatchers

    companion object {
        @AppScope
        @Provides
        fun provideNotificationHelper(context: Context): NotificationHelper {
            return NotificationHelper(context)
        }

        @AppScope
        @Provides
        fun provideDatabase(context: Context): TasksDatabase {
            return Room.databaseBuilder(
                context,
                TasksDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}