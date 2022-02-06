package com.suganth.cocktaildb.fragments

import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.suganth.cocktaildb.DrinksActivity
import com.suganth.cocktaildb.R
import com.suganth.cocktaildb.viewmodel.DrinkViewModel
import kotlinx.android.synthetic.main.fragment_item_details.*


class ItemDetailsFragment : Fragment(R.layout.fragment_item_details) {
    lateinit var viewModel:DrinkViewModel
    val args : ItemDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as DrinksActivity).viewModel
        initView()
    }

    private fun initView() {
        val drink = args.drinks
        itemName.text = args.drinks.strDrink
        val whiteColor = context?.let { ContextCompat.getColor(it,R.color.color_white) }
        val category = whiteColor?.let {
            SpannableStringBuilder()
                .color(it) {append("Category : ")}
                .append(" ")
                .append("${args.drinks.strCategory}")
        }
        tvCategory.text = category
        engInstruction.text = args.drinks.strInstructions

        Glide.with(this).load(args.drinks.strDrinkThumb).into(itemImg)

        addToFavorite.setOnClickListener {
            viewModel.saveFavoriteDrinks(drink)
            Toast.makeText(requireContext(),"Added successfully", Toast.LENGTH_LONG).show()
        }
    }
}