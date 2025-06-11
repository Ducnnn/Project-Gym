package com.example.projectgym

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ContentView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MealsMenuFragment : Fragment(), MealsMenuAdapter.RecyclerViewEvent {
    private val chicken = Products ("Chicken", 237, 23, 8, 0)
    private val bread = Products("Bread", 200, 4, 17, 20)
    private val kolbasa = Products("Kolbasa", 300, 34, 8, 9)
    private val cucumber = Products("Cucumber", 5, 20, 30, 6)

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

        val btn_constructor = view.findViewById<Button>(R.id.btn_add)
        btn_constructor.setOnClickListener{
            findNavController().navigate((R.id.action_mealsMenuFragment_to_mealsConstructorFragment))
        }

        val btn = view.findViewById<Button>(R.id.button_param)
        btn.setOnClickListener{
            findNavController().navigate(R.id.action_mealsMenuFragment_to_parametersFragment)
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
        proteins.text = "Proteins: ${totalProt}g"
        carbs.text = "Carbohydrates: ${totalCar}g"
        fats.text = "Fats: %.1f ${totalFat}g"
        calories.text = "Calories: %.1f ${totalCal}kcal"
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

        proteinProgress.progress = totalProtein.coerceAtMost(proteinGoal)
        fatsProgress.progress = totalFats.coerceAtMost(fatsGoal)
        carbsProgress.progress = totalCarbs.coerceAtMost(carbsGoal)
        caloriesProgress.progress = totalCalories.coerceAtMost(caloriesGoal)
    }

}
