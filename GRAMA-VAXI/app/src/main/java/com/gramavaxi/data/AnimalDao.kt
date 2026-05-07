package com.gramavaxi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao {
    @Insert
    suspend fun insert(animal: Animal)

    @Query("SELECT * FROM animals ORDER BY nextVaccinationDateMillis ASC")
    fun getAnimals(): Flow<List<Animal>>
}
