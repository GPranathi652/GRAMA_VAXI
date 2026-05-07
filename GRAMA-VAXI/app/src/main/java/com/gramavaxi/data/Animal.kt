package com.gramavaxi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animals")
data class Animal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val breed: String,
    val age: Int,
    val lastVaccinationDateMillis: Long,
    val nextVaccinationDateMillis: Long
)
