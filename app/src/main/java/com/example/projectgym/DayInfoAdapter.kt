package com.example.projectgym

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DayInfoAdapter(private val lst: List<Exercise>) :
    RecyclerView.Adapter<DayInfoAdapter.DayInfoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.current_day_row, parent, false)
        return DayInfoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    override fun onBindViewHolder(holder: DayInfoViewHolder, position: Int) {
        val exercise = lst[position]
        holder.tvExercise.text = exercise.name
        val setsAdapter = ArrayAdapter(
            holder.itemView.context,
            android.R.layout.simple_spinner_item,
            exercise.sets.indices.map { i -> i + 1 })
        setsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        holder.spinnerSets.adapter = setsAdapter
        holder.spinnerSets.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedValue = parent.getItemAtPosition(position).toString().toInt()
                holder.edTextReps.setText(exercise.sets[selectedValue - 1].reps.toString())
                holder.edTextWeight.setText(exercise.sets[selectedValue - 1].weight.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
    class DayInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvExercise = view.findViewById<TextView>(R.id.tv_current_day_exercise_name)
        val edTextWeight = view.findViewById<EditText>(R.id.edtext_weight)
        val edTextReps = view.findViewById<EditText>(R.id.edtext_reps)
        val spinnerSets = view.findViewById<Spinner>(R.id.spinner_sets)
    }
}