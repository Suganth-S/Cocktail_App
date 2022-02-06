package com.suganth.cocktaildb.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.suganth.cocktaildb.DrinksActivity
import com.suganth.cocktaildb.DrinksAdapter
import com.suganth.cocktaildb.R
import com.suganth.cocktaildb.util.Constant.Companion.SEARCH_DELAY
import com.suganth.cocktaildb.util.Resource
import com.suganth.cocktaildb.viewmodel.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_search_drinks.*
import kotlinx.android.synthetic.main.item_error_message.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchDrinksFragment : Fragment(R.layout.fragment_search_drinks) {
    private lateinit var viewModel: DrinkViewModel
    private lateinit var drinksAdapter: DrinksAdapter
    var isError = false
    var isLoading = false
    val TAG = "SearchDrinksFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as DrinksActivity).viewModel
        setUpSearchResultView()
        initView()
        initViewModel()
    }

    private fun initView() {
        drinksAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("drinks", it)
            }

            findNavController().navigate(
                R.id.action_searchDrinksFragment_to_itemDetailsFragment,
                bundle
            )
        }

        var job: Job? = null
        et_search.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty() && editable.toString().isNotBlank()) {
                        viewModel.getSearchItems(editable.toString())
                    }
                }
            }
        }

        btnRetry.setOnClickListener {
            if (et_search.text.toString().isNotEmpty()) {
                viewModel.getSearchItems(et_search.text.toString())
            } else {
                hideErrorMessage()
            }
        }
    }

    private fun initViewModel() {
        viewModel.searchDrink.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { drinksResponse ->
                        hideProgressBar()
                        drinksAdapter.differItem.submitList(drinksResponse.drinks.toList())
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

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
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
        rvSearchDrinks.apply {
            adapter = drinksAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }
}