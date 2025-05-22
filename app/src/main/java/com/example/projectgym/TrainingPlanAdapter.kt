package com.example.projectgym

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrainingPlanAdapter (private val list: List<TranDay>):
    RecyclerView.Adapter<TrainingPlanAdapter.TrainingPlanViewHolder>() {

    private var onLongClickListener: OnLongClickListener? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrainingPlanAdapter.TrainingPlanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.training_menu_row, parent, false)
        return TrainingPlanViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TrainingPlanAdapter.TrainingPlanViewHolder,
        position: Int
    ) {
        val day = list[position]
        holder.textView.text = day.name
        holder.itemView.setOnLongClickListener {
            onLongClickListener?.onLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnLongClickListener(listener: OnLongClickListener?) {
        this.onLongClickListener = listener
    }

    interface OnLongClickListener {
        fun onLongClick(position: Int)
    }

    class TrainingPlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val btnSettings = view.findViewById<ImageButton>(R.id.btn_settings)
        val textView = view.findViewById<TextView>(R.id.tv_day_name)


    }

}