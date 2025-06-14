package com.example.projectgym



class User(

    var name: String,
    var age: Float,
    var height: Float,
    var weight: Float,
    var gender: String,
    var goal: String,
    var program: MutableList<TranDay>
)

data class TranDay(
    var name: String? = null,
    var exercises: MutableList<Exercise> = mutableListOf(),
    var color: String? = null
) {
    init {
        if (this.name == null) {
            this.name = "Rest"

        }
        if (this.color == null) {
            this.color = "#ffa9a3"
        }
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
    var name: String = "",
    var muscle: String = "Chest",
    var description: String = "",
    var sets: MutableList<Set> = mutableListOf(Set(0, 0)),
    var isCompleted : Boolean = false
){
    fun clearSets(){
        for(set in sets){
            set.reps = 0
            set.weight = 0
            isCompleted = false
        }
    }
}

class Set(var reps: Int = 0, var weight: Int = 0)


data class ListMeals(
    var meals: MutableList<Meals> = mutableListOf()
) {
    fun addMeal(meal: Meals) {
        meals.add(meal)
    }
}

data class Meals(
    var name: String,
    var products: MutableList<Products> = mutableListOf()
)

data class Products(
    var name: String,
    var calories: Int,
    var proteins: Int,
    var fats: Int,
    var carbs: Int
)

data class MacroResult(
    var protein: Double,
    var fats: Double,
    var carbs: Double,
    var calories: Double
)



