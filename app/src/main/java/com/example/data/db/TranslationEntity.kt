package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translations")
data class TranslationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalText: String,
    val translatedEmail: String,
    val toneStyle: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
