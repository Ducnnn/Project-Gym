package com.example.projectgym

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText


class MealsConstructorFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_meals_constructor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnFoodCntstr = view.findViewById<Button>(R.id.food_cnstr_btn)
        btnFoodCntstr.setOnClickListener{
            showFoodBuilder(view)
        }
    }
    private fun showFoodBuilder(view: View){
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_products, null)

        val builder = AlertDialog.Builder(view.context)
            .setTitle("Add meal")
            .setView(dialogView)
            .setPositiveButton("Add") {_,_->
        }
    }
}
