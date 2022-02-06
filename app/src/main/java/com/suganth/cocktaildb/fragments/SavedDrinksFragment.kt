package com.suganth.cocktaildb.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.suganth.cocktaildb.DrinksActivity
import com.suganth.cocktaildb.DrinksAdapter
import com.suganth.cocktaildb.R
import com.suganth.cocktaildb.viewmodel.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_saved_drinks.*

class SavedDrinksFragment : Fragment(R.layout.fragment_saved_drinks) {
    lateinit var viewModel: DrinkViewModel
    lateinit var drinksAdapter: DrinksAdapter

    val TAG = "SavedDrinksFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as DrinksActivity).viewModel
        setUpRecyclerView()
        initView()
        initViewModel()
    }

    private fun initView() {

        drinksAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("drinks", it)
            }

            findNavController().navigate(
                R.id.action_savedDrinksFragment_to_itemDetailsFragment,
                bundle
            )
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val drinks = drinksAdapter.differItem.currentList[position]
                viewModel.deleteSavedDrinks(drinks)
                view?.let {
                    Snackbar.make(it, "Item Deleted Successfully", Snackbar.LENGTH_LONG).apply {
                        setAction("Undo") {
                            viewModel.saveFavoriteDrinks(drinks)
                        }
                        show()
                    }
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedDrinks)
        }
    }

    private fun initViewModel() {
        viewModel.getSavedDrinks().observe(viewLifecycleOwner, Observer { drinks ->
            if (drinks.isNotEmpty()) {
                drinksAdapter.differItem.submitList(drinks)
                noItems.visibility = View.GONE
            } else {
                noItems.visibility = View.VISIBLE
            }
        })
    }

    private fun setUpRecyclerView() {
        drinksAdapter = DrinksAdapter()
        rvSavedDrinks.apply {
            adapter = drinksAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }
}