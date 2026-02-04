package com.serona.app.data.mapper

import com.serona.app.data.dto.UserDataResponse
import com.serona.app.data.local.room.entity.UserEntity
import com.serona.app.data.model.Gender
import com.serona.app.data.model.User

// Fungsi untuk mengubah Respon API ke Tabel Database (Entity)
fun UserDataResponse.toEntity(): UserEntity {
    return UserEntity(
        name = this.name ?: "",
        email = this.email ?: "",
        gender = this.gender ?: "",
        country = this.country ?: "",
        birthDate = this.birth_date ?: "",
        faceShape = this.face_shape,
        skinTone = this.skin_tone
    )
}

// Fungsi untuk mengubah Tabel Database ke Model UI
fun UserEntity.toDomain(): User {
    return User(
        name = this.name,
        email = this.email,
        gender = if (this.gender.lowercase().trim() == "male") Gender.MALE else Gender.FEMALE,
        country = this.country,
        birthDate = this.birthDate,
        faceShape = this.faceShape,
        skinTone = this.skinTone
    )
}

// Fungsi pembantu untuk update data cicilan agar tidak jadi NULL
fun UserEntity.updateWith(
    name: String? = null,
    gender: String? = null,
    country: String? = null,
    birthDate: String? = null,
    faceShape: String? = null,
    skinTone: String? = null
): UserEntity {
    return this.copy(
        name = name ?: this.name,
        gender = gender ?: this.gender,
        country = country ?: this.country,
        birthDate = birthDate ?: this.birthDate,
        faceShape = faceShape ?: this.faceShape,
        skinTone = skinTone ?: this.skinTone
    )
}