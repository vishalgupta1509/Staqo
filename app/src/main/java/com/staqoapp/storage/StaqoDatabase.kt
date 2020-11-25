package com.staqoapp.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.staqoapp.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class StaqoDatabase: RoomDatabase() {
    abstract val mainDao: MainDao
}