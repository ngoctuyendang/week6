package com.example.week6.ROOM

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.week6.DATABASE_NAME


@Database(entities = arrayOf(User::class), version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDAO(): TaskDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, DATABASE_NAME
        ).allowMainThreadQueries()
            .build()
    }
}