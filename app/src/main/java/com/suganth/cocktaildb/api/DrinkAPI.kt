package com.suganth.cocktaildb.api

import com.suganth.cocktaildb.model.DrinkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface DrinkAPI {
    @GET("search.php")
    suspend fun searchForDrinks(
        @Query("s")
        searchQuery: String,
    ): Response<DrinkResponse>

    @GET("filter.php")
    suspend fun filterDrinks(
        @Query("a")
        filterQuery: String,
    ): Response<DrinkResponse>
}