package com.example.projectgym

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth

        val btnSignUp = findViewById<Button>(R.id.btn_main_signup)
        val btnSignIn = findViewById<Button>(R.id.btn_main_signin)

        btnSignUp.setOnClickListener {
            val signUpActivity = Intent(this, SignUpActivity::class.java)
            startActivity(signUpActivity)
        }
        btnSignIn.setOnClickListener {
            val signInActivity = Intent(this, SignInActivity::class.java)
            startActivity(signInActivity)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("auth", currentUser.uid)
            val mainMenuActivity = Intent(this, MainMenuActivity::class.java)
            startActivity(mainMenuActivity)
        }
    }


}
