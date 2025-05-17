package com.example.projectgym

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.VERBOSE
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val SIGN_UP_MESSAGE_PASSWORD_WRONG =
        "Password must be at least 6 characters long and may contain only uppercase letters, lowercase letters, numbers and symbols: ~`!@#\$%^&*()_-+={[}]|\\:;\"'<,>.?/"
    private val SIGN_UP_MESSAGE_EMAIL_WRONG =
        "Email should be yourname@website.domain"
    private val allowedCharacters =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~`!@#\$%^&*()_-+={[}]|\\:;\"'<,>.?/"
    private val TITLE_WRONG_SYMBOLS_IN_PASSWORD = "Wrong symbols in password"
    private val TITLE_EMAIL_IS_NOT_VALID = "Email is not valid"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        val btn = findViewById<Button>(R.id.btn_signup)
        val editTextEmail = findViewById<EditText>(R.id.edtext_email_signUp)
        val editTextPassword = findViewById<EditText>(R.id.edtext_password_signUp)
        btn.setOnClickListener {
//          TODO: implement a name adding feature
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            createAccount(email, password)
        }

    }


    private fun createAccount(email: String, password: String) {
        if (!checkCredentials(email, password)) {
            return
        }

        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
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
        } catch (e: Exception) {
            Log.w("signup", "Exception: $e")
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        if (password.length < 6) {
            return false
        }
        for (letter in password) {
            if (!allowedCharacters.contains(letter)) {
                return false
            }
        }
        return true
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private fun checkCredentials(email: String, password: String): Boolean {
        if (!isEmailValid(email)) {
            AlertDialog.Builder(this)
                .setMessage(SIGN_UP_MESSAGE_EMAIL_WRONG)
                .setTitle(TITLE_EMAIL_IS_NOT_VALID)
                .setPositiveButton("OK") { _, _ ->
                    {}
                }.create().show()
            return false
        }
        if (!isPasswordValid(password)) {
            AlertDialog.Builder(this)
                .setMessage(SIGN_UP_MESSAGE_PASSWORD_WRONG)
                .setTitle(TITLE_WRONG_SYMBOLS_IN_PASSWORD)
                .setPositiveButton("OK") { _, _ ->
                    {}
                }.create().show()
            return false
        }
        return true

    }
}