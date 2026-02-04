package com.serona.app.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey val email: String,
    val name: String,
    val gender: String,
    val country: String,
    val birthDate: String,
    val faceShape: String?,
    val skinTone: String?
)