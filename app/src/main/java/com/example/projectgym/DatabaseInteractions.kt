package com.example.projectgym

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kizitonwose.calendar.core.WeekDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class DatabaseInteractions {

    fun addUser(
        db: FirebaseFirestore,
        userId: String,
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

    fun addDayToWeek(db: FirebaseFirestore, path: String, day: TranDay, date: WeekDay) {
        val docRef = db.document("$path${date.date}")
        val dayData = hashMapOf(
            "color" to day.color,
            "dayName" to day.name,
            "dayOfWeek" to date.date.dayOfWeek
        )
        docRef.set(dayData).addOnSuccessListener { Log.i("addDayToWeek", "Success: ${date.date}") }

    }

    suspend fun getDay(db: FirebaseFirestore, path: String, date: WeekDay): TranDay {
        val docPath = db.document("$path${date.date}")
        val name: String?
        val color: String?
        return try{
            val documentSnapshot = withContext(Dispatchers.IO) {
                docPath.get().await()
            }
            if (documentSnapshot.exists()){
                name = documentSnapshot.getString("dayName")
                color = documentSnapshot.getString("color")
            } else {
                name = "Rest"
                color = "#ffa9a3"
            }
            return TranDay(name, color=color)
        }catch (e: Exception) {
            TranDay(null)
        }
    }
}