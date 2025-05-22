package com.example.projectgym

class User (
    var name: String,
    var age: Float,
    var height: Float,
    var weight: Float,
    var gender: String,
    var program: MutableList<TranDay>
)

class TranDay (
    var name: String,
    var exercises: MutableList<Exercise> = mutableListOf<Exercise>(),
) {
    fun addExercise(exercise : Exercise) {
        exercises.add(exercise)
    }

}

class Exercise (
    var name: String,
    var sets: Int,
    var muscle: String
)

class CurDay (
    // var trainingDay
    // val date
)

class ListMeals (
    var meals: MutableList<Meals> = mutableListOf()
) {
    fun addMeal(meal: Meals) {
        meals.add (meal)
    }
}

class Meals (
    var name: String,
    var products: MutableList<Products> = mutableListOf()
) {
    fun addProducts(product: Products) {
        products.add(product)
    }
}

class Products (
    var name: String,
    var calories: Float,
    var proteins: Float,
    var fats: Float,
    var carbs: Float
)

