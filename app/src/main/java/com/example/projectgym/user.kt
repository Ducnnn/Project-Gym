package com.example.projectgym

import kotlinx.datetime.DayOfWeek

class User(
    var name: String,
    var age: Float,
    var height: Float,
    var weight: Float,
    var gender: String,
    var program: MutableList<TranDay>
)

class TranDay(
    var name: String?,
    var exercises: MutableList<Exercise> = mutableListOf<Exercise>(),
    var color: String? = "#FFFFFF"
) {
    init {
        if (this.name == null) {
            this.name = "Rest"

        }
        if (this.color == null) {
            this.color = "#ffa9a3"
        }
    }
    fun addExercise(exercise: Exercise) {
        exercises.add(exercise)
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TranDay

        if (name != other.name) return false
        if (exercises != other.exercises) return false
        if (color != other.color) return false

        return true
    }
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

class Exercise(
    var name: String,
    var sets: Int,
    var muscle: String
)

class CurDay(
    // var trainingDay
    // val date
)

class ListMeals(
    var meals: MutableList<Meals> = mutableListOf()
) {
    fun addMeal(meal: Meals) {
        meals.add(meal)
    }
}

class Meals(
    var name: String,
    var products: MutableList<Products> = mutableListOf()
) {
    fun addProducts(product: Products) {
        products.add(product)
    }
}

class Products(
    var name: String,
    var calories: Float,
    var proteins: Float,
    var fats: Float,
    var carbs: Float
)

