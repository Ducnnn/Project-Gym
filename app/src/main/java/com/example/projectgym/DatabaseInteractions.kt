package com.example.projectgym

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

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
}