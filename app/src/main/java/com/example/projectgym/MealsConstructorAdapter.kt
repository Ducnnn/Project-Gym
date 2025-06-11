package com.example.projectgym

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MealsConstructorAdapter (private val list: List<Products>): RecyclerView.Adapter<MealsConstructorAdapter.ItemViewHolder> () {
    inner class ItemViewHolder(view:View): RecyclerView.ViewHolder(view) {
        val textView: TextView = itemView.findViewById(R.id.products_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_products_row, parent, false)

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val product = list[position]
        holder.textView.text = product.name
    }
}