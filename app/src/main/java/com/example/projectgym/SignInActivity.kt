package com.example.projectgym

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()
        val btn = findViewById<Button>(R.id.btn_signin)
        val editTextEmail = findViewById<EditText>(R.id.edtext_email_signIn)
        val editTextPassword = findViewById<EditText>(R.id.edtext_password_signIn)
        btn.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            logIn(email, password)
        }
    }

    private fun logIn(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("signin", "signInUserWithEmail:success")
                    val mainMenuActivity = Intent(this, MainMenuActivity::class.java)
                    startActivity(mainMenuActivity)

                } else {
                    Log.w("signin", "signInUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "SignIn failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        } catch (e: Exception) {
            Log.w("signin", "Exception: $e")
        }
    }
}