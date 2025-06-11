package com.example.projectgym

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.kizitonwose.calendar.core.WeekDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class DatabaseInteractions {
    private val db = Firebase.firestore
    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val pathToUser = "users/$userId/"

    fun addUser(
        email: String,
        displayName: String,
        createdAt: FieldValue
    ) {
        val userData = hashMapOf(
            "email" to email,
            "displayName" to displayName,
            "createdAt" to createdAt,
            "gender" to "male",
            "height" to 180,
            "weight" to 80,
            "goal" to "maintenance",
            "age" to 18,
            "activity" to "average",
            "macroGoals" to MacroResult(protein = 150.0, fats = 100.0, carbs = 200.0, calories = 1500.0)
        )
        db.collection("users").document(userId)
            .set(userData)
            .addOnSuccessListener {
                Log.d("FIRESTORE", "User data successfully written for UID: $userId")
            }
            .addOnFailureListener { e ->
                Log.w("FIRESTORE", "Error writing user data for UID: $userId", e)
            }
    }

    fun updateParameters(
        activity:String,
        gender: String,
        height: Int,
        weight: Int,
        goal: String,
        age: Int
    ) {
        val updatedUserData = hashMapOf<String, Any>(
            "activity" to activity,
            "gender" to gender,
            "height" to height,
            "weight" to weight,
            "goal" to goal,
            "age" to age
        )
        db.collection("users").document(userId).update(updatedUserData)
    }

    fun setRecommendedMacros (
        macroGoals: MacroResult
    ) {

        db.collection("users").document(userId).update(mapOf("macroGoals" to macroGoals))
    }


    fun addDayToWeek(day: TranDay, date: WeekDay) {
        val docRef = db.document("${pathToUser}TrainingDays/${date.date}")
        try {
            docRef.set(day).addOnSuccessListener { Log.i("addDayToWeek", "Success: ${date.date}") }
        } catch (e: Exception) {
            Log.e("Database addDayToWeek", "Failed to add day, exception: ${e.message}")
        }
    }

    suspend fun getTrainingDay(date: WeekDay): TranDay {
        val docPath = db.document("${pathToUser}TrainingDays/${date.date}")
        return try {
            val documentSnapshot = withContext(Dispatchers.IO) {
                docPath.get().await()
            }
            if (documentSnapshot.exists()) {
                val trainingDay = documentSnapshot.toObject(TranDay::class.java)
                if (trainingDay != null) {
                    return trainingDay
                }
            }
            TranDay("Rest", color = "#ffa9a3")
        } catch (e: Exception) {
            TranDay("Rest", color = "#ffa9a3")
        }
    }

    fun addCustomDayToTrainingProgram(day: TranDay) {
        val docRef = db.document("${pathToUser}CustomDays/${day.name}")
        docRef.set(day).addOnSuccessListener { Log.i("addCustomDay", "Success") }
    }

    fun deleteCustomDayFromTrainingProgram(day: TranDay) {
        val docRef = db.document("${pathToUser}CustomDays/${day.name}")
        docRef.delete().addOnSuccessListener { Log.i("deleteCustomDay", "Success") }
    }

    suspend fun getTrainingProgram(): List<TranDay> {
        val docRef = db.collection("${pathToUser}CustomDays")
        val result = mutableListOf<TranDay>()
        try {
            val documentQuery = withContext(Dispatchers.IO) {
                docRef.get().await()
            }
            if (documentQuery.isEmpty) {
                return emptyList()
            }
            for (document in documentQuery) {
                try {
                    val day = document.toObject(TranDay::class.java)
                    result.add(day)
                    Log.d("FIRESTORE_COROUTINE", "Fetched day: ${day.name}, ID: ${document.id}")
                } catch (e: Exception) {
                    Log.e("FIRESTORE_COROUTINE", "Error converting document ${document.id}", e)
                }
            }
        } catch (e: Exception) {
            Log.e("Database", "Failed to get program list")
        }
        return result
    }

    fun deleteTrainingDay(date: WeekDay) {
        val docPath = db.document("${pathToUser}TrainingDays/${date.date}")
        docPath.delete()
    }

    fun updateNumberOfSets(date: WeekDay, exercises: MutableList<Exercise>) {
        val docPath = db.document("${pathToUser}TrainingDays/${date.date}")
        docPath.update("exercises", exercises)
    }
}