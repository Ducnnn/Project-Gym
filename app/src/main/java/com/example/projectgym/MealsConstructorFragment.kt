package com.example.projectgym

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate


class MealsConstructorFragment : Fragment() {
    private var listOfProducts = mutableListOf<Products>()
    private lateinit var mealsConstructorAdapter: MealsConstructorAdapter
    val currentDate = LocalDate.now()
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
        val btnAddMeal = view.findViewById<Button>(R.id.btn_add)
        btnFoodCntstr.setOnClickListener{
            showFoodBuilder(view)
        }
        btnAddMeal.setOnClickListener{
            val edMealName = view.findViewById<EditText>(R.id.mealconstructortext)
            val mealName = edMealName.text.toString()
            val meal = Meals(mealName, listOfProducts)
            addMealToCurrentDay(meal, currentDate)
            findNavController().navigate(R.id.action_mealsConstructorFragment_to_mealsMenuFragment)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewProduct)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        mealsConstructorAdapter = MealsConstructorAdapter(listOfProducts)
        recyclerView.adapter = mealsConstructorAdapter

    }
    private fun showFoodBuilder(view: View) {
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_products, null)

        val builder = AlertDialog.Builder(view.context)
            .setTitle("Add product")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val edProductName = dialogView.findViewById<EditText>(R.id.nameOfProduct).text.toString()
                val edProductCalories = dialogView.findViewById<EditText>(R.id.caloriesProduct).text.toString().toInt()
                val edProductFats = dialogView.findViewById<EditText>(R.id.fatsProduct).text.toString().toInt()
                val edProductCarbs = dialogView.findViewById<EditText>(R.id.carbsProduct).text.toString().toInt()
                val edProductProteins = dialogView.findViewById<EditText>(R.id.proteinsProduct).text.toString().toInt()
                addProductToTheList(edProductName, edProductCalories, edProductFats, edProductCarbs, edProductProteins)
            }
            .setNegativeButton("Cancel") {dialog, _->
                dialog.dismiss()
            }
        val alertDialog: AlertDialog = builder.create()

        alertDialog.show()
    }
    private fun addProductToTheList(
        name: String,
        calories: Int,
        proteins: Int,
        fats: Int,
        carbs: Int
    ) {
        val product = Products(name, calories, proteins, fats, carbs)
        listOfProducts.add(product)
        mealsConstructorAdapter.notifyItemInserted(mealsConstructorAdapter.itemCount)
    }
    private fun addMealToCurrentDay(meals :Meals, date: LocalDate) {
        DatabaseInteractions().addMealToDay(date, meals)
    }
}
