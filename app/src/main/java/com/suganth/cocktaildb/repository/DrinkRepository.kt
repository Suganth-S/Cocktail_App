package com.suganth.cocktaildb.repository

import android.widget.SearchView
import com.suganth.cocktaildb.api.RetrofitInstance
import com.suganth.cocktaildb.db.DrinkDatabase
import com.suganth.cocktaildb.model.Drink

class DrinkRepository(
    val db: DrinkDatabase
) {

    suspend fun searchForDrinks(searchQuery: String) =
        RetrofitInstance.api.searchForDrinks(searchQuery)

    suspend fun filterDrinks(filterQuery: String) = RetrofitInstance.api.filterDrinks(filterQuery)

    suspend fun upsert(drink: Drink) = db.getDrinksDao().upsert(drink)

    suspend fun delete(drink: Drink) = db.getDrinksDao().deleteDrinks(drink)

    fun getDrinks() = db.getDrinksDao().getAllDrinks()
}