package com.suganth.cocktaildb.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.suganth.cocktaildb.model.Drink

@Dao
interface DrinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(drink: Drink): Long

    @Query("SELECT * FROM drinks")
    fun getAllDrinks(): LiveData<List<Drink>>

    @Delete
    suspend fun deleteDrinks(drink: Drink)
}