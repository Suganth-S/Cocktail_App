package com.suganth.cocktaildb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.suganth.cocktaildb.db.DrinkDatabase
import com.suganth.cocktaildb.repository.DrinkRepository
import com.suganth.cocktaildb.viewmodel.DrinkViewModel
import kotlinx.android.synthetic.main.activity_drinks.*

class DrinksActivity : AppCompatActivity() {

    lateinit var viewModel: DrinkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drinks)

        val drinksRepository = DrinkRepository(DrinkDatabase(this))
        val viewModelProviderFactory = DrinksViewModelProviderFactory(application, drinksRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(DrinkViewModel::class.java)
        bottomNavigationView.setupWithNavController(drinksNavHostFragment.findNavController())
    }
}