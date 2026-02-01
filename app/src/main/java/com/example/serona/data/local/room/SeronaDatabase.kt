package com.example.serona.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.serona.data.local.room.dao.UserDao
import com.example.serona.data.local.room.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class SeronaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}