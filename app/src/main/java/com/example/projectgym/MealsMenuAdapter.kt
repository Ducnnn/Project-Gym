package com.example.projectgym

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MealsMenuAdapter (private val list: List<Meals>, private val listener: RecyclerViewEvent) : RecyclerView.Adapter<MealsMenuAdapter.ItemViewHolder> () {
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val textView: TextView = itemView.findViewById(R.id.meals_name)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_meals_row, parent, false)

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val meal = list[position]
        holder.textView.text = meal.name
    }

    interface RecyclerViewEvent {
        fun onItemClick(position: Int)
    }
}