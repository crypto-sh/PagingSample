package com.pagingsampleproject.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.pagingsampleproject.model.RepoSearch


@Database(entities = [RepoSearch::class], exportSchema = false, version = 1)
abstract class RepoDataBase : RoomDatabase() {

    abstract fun reposDao(): RepoDao

    companion object {

        @Volatile
        private var INSTANCE: RepoDataBase? = null

        fun getInstance(context: Context): RepoDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, RepoDataBase::class.java, "Github.db").build()
    }

}
