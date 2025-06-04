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
            "createdAt" to createdAt
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

    fun addDayToWeek(day: TranDay, date: WeekDay) {
        val docRef = db.document("${pathToUser}TrainingDays/${date.date}")
        val dayData = hashMapOf(
            "color" to day.color,
            "dayName" to day.name,
            "dayOfWeek" to date.date.dayOfWeek
        )
        docRef.set(dayData).addOnSuccessListener { Log.i("addDayToWeek", "Success: ${date.date}") }

    }

    suspend fun getTrainingDay(date: WeekDay): TranDay {
        val docPath = db.document("${pathToUser}TrainingDays/${date.date}")
        val name: String?
        val color: String?
        return try {
            val documentSnapshot = withContext(Dispatchers.IO) {
                docPath.get().await()
            }
            if (documentSnapshot.exists()) {
                name = documentSnapshot.getString("dayName")
                color = documentSnapshot.getString("color")
            } else {
                name = "Rest"
                color = "#ffa9a3"
            }
            return TranDay(name, color = color)
        } catch (e: Exception) {
            TranDay(null)
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

        } catch (e : Exception) {
            Log.e("Database", "Failed to get program list")
        }

        return result
    }
    fun deleteTrainingDay(date: WeekDay){
        val docPath = db.document("${pathToUser}TrainingDays/${date.date}")
        docPath.delete()
    }
}