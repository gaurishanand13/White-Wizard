package com.example.whitewizard.Room.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.whitewizard.Room.Daos.imageDao
import com.example.whitewizard.Room.Models.imageModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Database(entities = arrayOf(imageModel::class), version = 1, exportSchema = false)
public abstract class myDatabase : RoomDatabase() {

    abstract fun myDao(): imageDao


    private class WordDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var wordDao = database.myDao()
                    //
                }
            }
        }
    }

    companion object {
        private var INSTANCE: myDatabase? = null
        fun getDatabase(context: Context): myDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, myDatabase::class.java, "word_database")
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}