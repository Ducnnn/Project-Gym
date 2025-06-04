package com.example.projectgym

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class DayConstructorAdapter(private val lst: List<Exercise>) :
    RecyclerView.Adapter<DayConstructorAdapter.DayConstructorViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DayConstructorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_constructor_row, parent, false)
        return DayConstructorViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DayConstructorViewHolder,
        position: Int
    ) {
        val exercise = lst[position]
        holder.textView.text = exercise.name
        holder.btnDelete.setOnClickListener {
            onClickListener?.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }

    class DayConstructorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnDelete = view.findViewById<ImageButton>(R.id.imgbtn_delete_row)
        val textView = view.findViewById<TextView>(R.id.tv_exercise_name_row)
    }
}