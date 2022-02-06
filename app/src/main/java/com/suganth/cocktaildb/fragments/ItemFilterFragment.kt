package com.suganth.cocktaildb.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.suganth.cocktaildb.DrinksActivity
import com.suganth.cocktaildb.DrinksAdapter
import com.suganth.cocktaildb.R
import com.suganth.cocktaildb.util.Resource
import com.suganth.cocktaildb.viewmodel.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_filter_items.*
import kotlinx.android.synthetic.main.fragment_search_drinks.*
import kotlinx.android.synthetic.main.item_error_message.*

class ItemFilterFragment : Fragment(R.layout.fragment_filter_items) {
    lateinit var viewModel: DrinkViewModel
    lateinit var drinksAdapter: DrinksAdapter
    var isError = false
    var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as DrinksActivity).viewModel
        setUpSearchResultView()
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.filterDrink.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        drinkResponse ->
                        hideProgressBar()
                        drinksAdapter.differItem.submitList(drinkResponse.drinks.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun initView() {
        val categoryOptions = arrayOf("Alcoholic", "Non_Alcoholic")
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, categoryOptions)
//            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryOptions)
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_ID.adapter = arrayAdapter

        spinner_ID.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.filterItems(categoryOptions[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(),"Nothing selected",Toast.LENGTH_LONG).show()
            }

        }
    }
    private fun hideProgressBar() {
        filterprogressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        filterprogressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        itemErrorMessage.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        itemErrorMessage.visibility = View.VISIBLE
        tvErrorMessage.text = message
        isError = true
    }

    private fun setUpSearchResultView() {
        drinksAdapter = DrinksAdapter()
        rvFiltered.apply {
            adapter = drinksAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }
}