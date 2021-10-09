package com.pashacabu.rentateamtestapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Users::class],
    version = 1
)
abstract class AppDB : RoomDatabase() {

    abstract fun usersDAO() : UsersDAO

    companion object{
        private const val DB_NAME = "APP_DB"

        fun createDB(context: Context) : AppDB{
            return Room.databaseBuilder(
                context,
                AppDB::class.java,
                DB_NAME
            ).build()
        }
    }
}