package com.suganth.cocktaildb.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "drinks")
data class Drink(
    @PrimaryKey
    val idDrink: Int,
    val strDrink: String?,
    val strAlcoholic: String?,
    val strCategory: String?,
    val strInstructions: String?,
    val strDrinkThumb: String?,
) : Serializable