package com.suganth.cocktaildb.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.suganth.cocktaildb.CocktailApplication
import com.suganth.cocktaildb.model.Drink
import com.suganth.cocktaildb.model.DrinkResponse
import com.suganth.cocktaildb.repository.DrinkRepository
import com.suganth.cocktaildb.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class DrinkViewModel(
    app: Application,
    val drinkRepository: DrinkRepository
) : AndroidViewModel(app) {

    val searchDrink: MutableLiveData<Resource<DrinkResponse>> = MutableLiveData()
    val filterDrink: MutableLiveData<Resource<DrinkResponse>> = MutableLiveData()
    var searchResponse: DrinkResponse? = null
    var newSearchQuery: String? = null
    var filterResponse: DrinkResponse? = null

    init {
        getSearchItems("a")
    }

     fun getSearchItems(searchQuery: String) = viewModelScope.launch {
        safeSearchDrinksCall(searchQuery)
    }

    private suspend fun safeSearchDrinksCall(searchQuery: String) {
        newSearchQuery = searchQuery
        searchDrink.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = drinkRepository.searchForDrinks(searchQuery)
                searchDrink.postValue(handleSearchDrinkResponse(response))
            }else{
                searchDrink.postValue(Resource.Error("No internet Connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> searchDrink.postValue(Resource.Error("Network Failure"))
                else -> searchDrink.postValue(Resource.Error("Conversion Error"))
            }
        }

    }

    private fun handleSearchDrinkResponse(response: Response<DrinkResponse>): Resource<DrinkResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(
                    searchResponse ?: resultResponse
                )
            }
        }
        return Resource.Error(response.message())
    }

    fun filterItems(filterQuery: String) = viewModelScope.launch {
        filterDrinksCall(filterQuery)
    }

    private suspend fun filterDrinksCall(filterQuery: String){
        filterDrink.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response = drinkRepository.filterDrinks(filterQuery)
                filterDrink.postValue(handleFilterResponse(response))
            }else{
                filterDrink.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> filterDrink.postValue(Resource.Error("Network Failure"))
                else -> filterDrink.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private fun handleFilterResponse(response: Response<DrinkResponse>): Resource<DrinkResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                resultResponse ->
                return Resource.Success(
                    filterResponse ?: resultResponse
                )
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<CocktailApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    fun saveFavoriteDrinks(drink: Drink)= viewModelScope.launch {
        drinkRepository.upsert(drink)
    }

    fun getSavedDrinks()  = drinkRepository.getDrinks()

    fun deleteSavedDrinks(drink: Drink) = viewModelScope.launch {
        drinkRepository.delete(drink)
    }
}