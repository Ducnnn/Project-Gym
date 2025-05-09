package com.example.projectgym

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.VERBOSE
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        val btn = findViewById<Button>(R.id.btn_signup)
        val editTextEmail = findViewById<EditText>(R.id.edtext_email_signUp)
        val editTextPassword = findViewById<EditText>(R.id.edtext_password_signUp)
        btn.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            createAccount(email, password)
        }

    }



    private fun createAccount(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("signup", "createUserWithEmail:success")
                    val mainMenuActivity = Intent(this, MainMenuActivity::class.java)
                    startActivity(mainMenuActivity)
                } else {
                    Log.w("signup", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Signup failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        } catch (e:Exception){
            Log.w("signup", "Exception: $e")
        }



    }
}