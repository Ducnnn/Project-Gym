package com.example.projectgym

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape

class DayConstructorFragment : Fragment() {
    private var listOfExercises = mutableListOf<Exercise>()
    private lateinit var dayConstructorAdapter: DayConstructorAdapter
    private var pickedColor = "#FFFFFF"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_dayConstructorFragment_to_trainingPlanMenuFragment)
        }
        callback.isEnabled
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_day_constructor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnAddExercise = view.findViewById<Button>(R.id.btn_add_exercise)
        btnAddExercise.setOnClickListener {
            showExerciseBuilder(view)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleview_day_constructor)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        dayConstructorAdapter = DayConstructorAdapter(listOfExercises)
        dayConstructorAdapter.setOnClickListener(object :
            DayConstructorAdapter.OnClickListener {
            override fun onClick(position: Int) {
                deleteExerciseFromTheList(position)
            }
        })
        recyclerView.adapter = dayConstructorAdapter

        val btnColorPicker = view.findViewById<ImageButton>(R.id.btn_color_picker)
        btnColorPicker.setOnClickListener {
            showColorPicker(view, btnColorPicker)
        }
        val btnAddDay = view.findViewById<Button>(R.id.btn_add_constructed_day)
        btnAddDay.setOnClickListener {
            val edTextDayTitle = view.findViewById<TextView>(R.id.editTextDayTitle)
            if (!edTextDayTitle.text.isNullOrEmpty()){
                val dayName = edTextDayTitle.text.toString()
                addDayToTrainingProgram(dayName, pickedColor)
                findNavController().navigate(R.id.action_dayConstructorFragment_to_trainingPlanMenuFragment)
            } else Toast.makeText(view.context,"Enter your day Title!", Toast.LENGTH_LONG).show()

        }
    }

    private fun showExerciseBuilder(view: View) {
        val inflater = requireActivity().layoutInflater;
        val dialogView = inflater.inflate(R.layout.dialog_exercise_constructor, null)
        val spinner = dialogView.findViewById<Spinner>(R.id.spinner_muscle_groups)
        ArrayAdapter.createFromResource(
            dialogView.context,
            R.array.muscle_groups_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        AlertDialog.Builder(view.context)
            .setTitle("Add Exercise")
            .setView(dialogView)
            .setPositiveButton("ADD") { _, _ ->
                val exerciseName =
                    dialogView.findViewById<TextView>(R.id.editText_exercise_name).text.toString()
                val muscleGroup = spinner.selectedItem.toString()
                val exerciseDescription =
                    dialogView.findViewById<TextView>(R.id.editText_exercise_description).text.toString()
                addExerciseToTheList(exerciseName, muscleGroup, exerciseDescription)

            }
            .setNegativeButton("CANCEL") { _, _ -> }
            .create().show()
    }

    private fun addExerciseToTheList(name: String, muscleGroup: String, description: String) {
        val exercise = Exercise(name, muscleGroup, description)
        listOfExercises.add(exercise)
        dayConstructorAdapter.notifyItemInserted(dayConstructorAdapter.itemCount)
    }

    private fun deleteExerciseFromTheList(position: Int) {
        listOfExercises.removeAt(position)
        dayConstructorAdapter.notifyItemRemoved(position)
        dayConstructorAdapter.notifyItemRangeChanged(position, listOfExercises.size)
    }

    private fun showColorPicker(view: View, button: ImageButton) {
        ColorPickerDialog
            .Builder(view.context)
            .setTitle("Pick Color")
            .setColorShape(ColorShape.SQAURE)
            .setDefaultColor(R.color.white)
            .setColorListener { color, colorHex ->
                button.setBackgroundColor(color)
                pickedColor = colorHex
            }
            .show()
    }

    private fun addDayToTrainingProgram(name: String, color: String) {
        DatabaseInteractions().addCustomDayToTrainingProgram(TranDay(name, listOfExercises, color))
    }
}
