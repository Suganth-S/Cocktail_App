package com.suganth.cocktaildb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.suganth.cocktaildb.model.Drink
import kotlinx.android.synthetic.main.drinks_item_preview.view.*

class DrinksAdapter : RecyclerView.Adapter<DrinksAdapter.DrinksViewHolder>() {

    var drinkList = mutableListOf<Drink>()
    private var onItemClickListener: ((Drink) -> Unit)? = null

    private val diffCallback = object : DiffUtil.ItemCallback<Drink>() {
        override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.strDrink == newItem.strDrink
        }

        override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem == newItem
        }
    }

    val differItem = AsyncListDiffer(this, diffCallback)

    fun setDataList(dataList: List<Drink>) {
        this.drinkList = dataList.toMutableList()
    }

    inner class DrinksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DrinksAdapter.DrinksViewHolder {
        return DrinksViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.drinks_item_preview,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DrinksViewHolder, position: Int) {
        var drinks = differItem.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(drinks.strDrinkThumb).transform(
                CenterCrop(),
                RoundedCorners(24)
            ).into(ivDrinks)
            tvDrinksName.text = drinks.strDrink
            tvCategory.text = drinks.strCategory

            setOnClickListener {
                onItemClickListener?.let { it(drinks) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Drink) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differItem.currentList.size
    }
}