package com.example.projectgym
import com.example.projectgym.MacroResult



class CalculatorForMacros {
    fun calculateMacros(
        activity:String,
        gender: String,
        height: Int,
        weight: Int,
        goal: String,
        age: Int
    ): MacroResult {
        val bmr = if (gender == "male") { //Mifflin-St Jeor Equation
            10 * weight + 6.25 * height - 5 * age + 5
        } else {
            10 * weight + 6.25 * height - 5 * age - 161
        }

        val activityLevel = if (activity == "Little- No Exercise") {
            1.2
        } else if (activity == "Light (1-3 days/week)") {
            1.4
        } else if (activity == "Average (3-5 days/week)") {
            1.5
        } else if (activity == "Active (6-7 days/week)") {
            1.7
        } else {
            1.5
        }


        val tdee = bmr * activityLevel

        val GoalCalories = if (goal == "cut") {
            tdee * 0.8
        } else if (goal == "bulk") {
            tdee * 1.2
        } else {
            tdee
        }

        val proteinGrams = if (goal == "cut") {
            2.4 * weight
        } else if (goal == "bulk") {
            1.8 * weight
        } else {
            1.2 * weight
        }

        val fatGrams = if (goal == "cut") {
            0.5 * weight
        } else if (goal == "bulk") {
            1.0 * weight
        } else {
            0.8 * weight
        }

        val proteinCals = proteinGrams * 4
        val fatCals = fatGrams * 9
        val carbCals = GoalCalories - (proteinCals + fatCals)
        val carbGrams = carbCals / 4

        return MacroResult(
            protein = String.format("%.1f", proteinGrams).toDouble(),
            fats = String.format("%.1f", fatGrams).toDouble(),
            carbs = String.format("%.1f", carbGrams).toDouble(),
            calories = String.format("%.1f", GoalCalories).toDouble(),
        )
    }
}