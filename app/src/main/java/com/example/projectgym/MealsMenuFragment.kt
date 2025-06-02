package com.example.projectgym

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ContentView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MealsMenuFragment : Fragment(), MealsMenuAdapter.RecyclerViewEvent {
    private val chicken = Products ("Chicken", 237.0, 23.28, 8.1, 0.0)
    private val bread = Products("Bread", 200.0, 4.0, 17.0, 20.0)
    private val kolbasa = Products("Kolbasa", 300.0, 34.0, 8.0, 9.0)
    private val cucumber = Products("Cucumber", 5.0, 20.0, 30.0, 6.0)

    private val syrnyky = Meals ("Syrnyky", mutableListOf(bread))
    private val varenyky = Meals ("Varenyky", mutableListOf(bread, chicken))
    private val pelmeni = Meals("Pelmeni", mutableListOf(bread, chicken))
    private val buter = Meals("Buter", mutableListOf(bread, kolbasa))
    private val gleb = Meals("Gleb", mutableListOf(cucumber, bread))
    private val salat = Meals("Salat", mutableListOf(cucumber, kolbasa))
    private val list = mutableListOf(syrnyky, varenyky, pelmeni, buter, gleb, salat)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_meals_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealsMenuAdapter = MealsMenuAdapter(list, this)

        val recyclerViewMeal = view.findViewById<RecyclerView>(R.id.recycler_meals)
        recyclerViewMeal.layoutManager = LinearLayoutManager(view.context)
        recyclerViewMeal.adapter = mealsMenuAdapter

        fillProgressBars(list, view)

        val proteinClick = view.findViewById<ProgressBar>(R.id.progress_proteins)
        proteinClick.setOnClickListener{

        }



    }

    override fun onItemClick(position: Int) {
        val meal = list[position]
        showCustomLayoutDialog(meal)
    }
    private fun showCustomLayoutDialog(meal: Meals) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_fragment_meals, null)
        val title = dialogView.findViewById<TextView>(R.id.meal_name_w)
        val proteins = dialogView.findViewById<TextView>(R.id.proteins)
        val carbs = dialogView.findViewById<TextView>(R.id.carbs)
        val fats = dialogView.findViewById<TextView>(R.id.fats)
        val calories = dialogView.findViewById<TextView>(R.id.calories)

        val totalProt = meal.products.sumOf{it.proteins}
        val totalCar = meal.products.sumOf{it.carbs}
        val totalFat = meal.products.sumOf { it.fats }
        val totalCal = meal.products.sumOf { it.calories }

        builder.setNeutralButton("Ok") { dialog,_->
            dialog.dismiss()
        }

        title.text = "Macros for the ${meal.name}"
        proteins.text = "Proteins: %.1f g".format(totalProt)
        carbs.text = "Carbohydrates: %.1f g".format(totalCar)
        fats.text = "Fats: %.1f g".format(totalFat)
        calories.text = "Calories: %.1f kcal".format (totalCal)
        builder.setView(dialogView)


        val alertDialog: AlertDialog = builder.create()

        alertDialog.show()
    }
    private val proteinGoal = 300
    private val fatsGoal = 500
    private val carbsGoal = 600
    private val caloriesGoal = 700

    private fun fillProgressBars (meals: List<Meals>, view: View) {
        val totalProtein = meals.sumOf { it.products.sumOf { product -> product.proteins } }
        val totalFats = meals.sumOf { it.products.sumOf { product -> product.proteins }}
        val totalCarbs = meals.sumOf { it.products.sumOf { product -> product.carbs } }
        val totalCalories = meals.sumOf { it.products.sumOf { product -> product.calories } }

        val proteinProgress = view.findViewById<ProgressBar>(R.id.progress_proteins)
        val fatsProgress = view.findViewById<ProgressBar>(R.id.progress_fats)
        val carbsProgress = view.findViewById<ProgressBar>(R.id.progress_carbohydrates)
        val caloriesProgress = view.findViewById<ProgressBar>(R.id.progress_calories)

        proteinProgress.max = proteinGoal
        fatsProgress.max = fatsGoal
        carbsProgress.max = carbsGoal
        caloriesProgress.max = caloriesGoal

        proteinProgress.progress = totalProtein.toInt().coerceAtMost(proteinGoal)
        fatsProgress.progress = totalFats.toInt().coerceAtMost(fatsGoal)
        carbsProgress.progress = totalCarbs.toInt().coerceAtMost(carbsGoal)
        caloriesProgress.progress = totalCalories.toInt().coerceAtMost(caloriesGoal)
    }

}
