package com.example.projectgym

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class CurrentDayAdapter(private val listOfExercises: List<Exercise>) :
    RecyclerView.Adapter<CurrentDayAdapter.CurrentDayViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentDayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.current_day_row, parent, false)
        return CurrentDayViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrentDayViewHolder, position: Int) {
        val exercise = listOfExercises[position]
        holder.tvExercise.text = exercise.name
        holder.tgButtonFinished.isClickable = true
        holder.edTextReps.isFocusableInTouchMode = true
        holder.edTextWeight.isFocusableInTouchMode = true
        holder.tgButtonFinished.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                exercise.isCompleted = true
                holder.cardLayout.setBackgroundResource(R.drawable.layout_bgggg)
                holder.edTextWeight.isFocusable = false
                holder.edTextReps.isFocusable = false
                holder.btnDeleteSet.isClickable = false
                holder.btnAddSet.isClickable = false
                holder.spinnerSets.isEnabled = false
            } else {
                exercise.isCompleted = false
                holder.cardLayout.setBackgroundResource(R.drawable.layout_bg)
                holder.edTextWeight.isFocusable = true
                holder.edTextReps.isFocusable = true
                holder.edTextWeight.isFocusableInTouchMode = true
                holder.edTextReps.isFocusableInTouchMode = true
                holder.edTextReps.isFocusable = true
                holder.btnDeleteSet.isClickable = true
                holder.btnAddSet.isClickable = true
                holder.spinnerSets.isEnabled = true
            }
        }


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

        holder.btnAddSet.setOnClickListener {
            if (!(holder.edTextWeight.text.isNullOrEmpty() && holder.edTextReps.text.isNullOrEmpty()) && (holder.edTextWeight.text.toString() != "0" || holder.edTextReps.text.toString() != "0") && holder.spinnerSets.selectedItemPosition == setsAdapter.count - 1) {
                exercise.sets[setsAdapter.count - 1] = Set(
                    holder.edTextReps.text.toString().toInt(),
                    holder.edTextWeight.text.toString().toInt()
                )
                exercise.sets.add(Set(0, 0))
                setsAdapter.insert(setsAdapter.count + 1, setsAdapter.count)
                holder.spinnerSets.setSelection(setsAdapter.count - 1)

            }
        }

        holder.edTextReps.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                val inputText = s.toString()

                if (inputText.isNotEmpty()) { // Or any other condition for "valid input"
                    exercise.sets[holder.spinnerSets.selectedItemPosition].reps = inputText.toInt()
                }
            }
        })
        holder.edTextWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                val inputText = s.toString()

                if (inputText.isNotEmpty()) { // Or any other condition for "valid input"
                    exercise.sets[holder.spinnerSets.selectedItemPosition].weight =
                        inputText.toInt()
                }
            }
        })
        holder.btnDeleteSet.setOnClickListener {
            if(holder.spinnerSets.count > 1){
                setsAdapter.remove(holder.spinnerSets.count)
                exercise.sets.removeAt(holder.spinnerSets.count)
            } else {
                exercise.sets[0].reps = 0
                exercise.sets[0].weight = 0
                holder.edTextReps.setText(exercise.sets[0].reps.toString())
                holder.edTextWeight.setText(exercise.sets[0].weight.toString())
            }
        }
        holder.tgButtonFinished.isChecked = exercise.isCompleted
        holder.tgButtonFinished.toggle()
        holder.tgButtonFinished.toggle()
    }

    override fun getItemCount(): Int {
        return listOfExercises.size
    }
    class CurrentDayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvExercise = view.findViewById<TextView>(R.id.tv_current_day_exercise_name)
        val tgButtonFinished = view.findViewById<ToggleButton>(R.id.tgbtn_finished)
        val imgExerciseIcon = view.findViewById<ImageView>(R.id.img_exercise_icon)
        val spinnerSets = view.findViewById<Spinner>(R.id.spinner_sets)
        val edTextWeight = view.findViewById<EditText>(R.id.edtext_weight)
        val edTextReps = view.findViewById<EditText>(R.id.edtext_reps)
        val cardLayout = view.findViewById<MaterialCardView>(R.id.layout_card)
        val btnAddSet = view.findViewById<ImageButton>(R.id.btn_add_set)
        val btnDeleteSet = view.findViewById<ImageButton>(R.id.btn_delete_set)
    }
}