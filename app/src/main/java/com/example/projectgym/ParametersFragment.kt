package com.example.projectgym

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class ParametersFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_parameters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genderSpinner = view.findViewById<Spinner>(R.id.spinnerGenderParam)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.SpinnerGender_items,
            android.R.layout.simple_spinner_item

        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter

        val goalSpinner = view.findViewById<Spinner>(R.id.SpinnerGoalParam)
        val adapter2 = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.SpinnerGoal_items,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        goalSpinner.adapter = adapter2

        val age = view.findViewById<EditText>(R.id.editTextText3)
        val height = view.findViewById<EditText>(R.id.editTextText2)
        val weight = view.findViewById<EditText>(R.id.editTextText)
        val gender = view.findViewById<Spinner>(R.id.spinnerGenderParam)
        val goal = view.findViewById<Spinner>(R.id.SpinnerGoalParam)

        val submitBtn = view.findViewById<Button>(R.id.btn_submit)
        submitBtn.setOnClickListener{
            if (age.text.isNotEmpty() && height.text.isNotEmpty() && weight.text.isNotEmpty())
                DatabaseInteractions().updateParameters(gender.selectedItem.toString(), height.text.toString().toInt()
                    ,weight.text.toString().toInt(), goal.selectedItem.toString(), age.text.toString().toInt())
                findNavController().navigate(R.id.action_parametersFragment_to_mealsMenuFragment)
        }
    }
}