package com.suganth.cocktaildb

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.suganth.cocktaildb.repository.DrinkRepository
import com.suganth.cocktaildb.viewmodel.DrinkViewModel

class DrinksViewModelProviderFactory(
    val app: Application,
    val drinksRepository: DrinkRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DrinkViewModel(app, drinksRepository) as T
    }
}