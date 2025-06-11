package com.example.projectgym

import android.R.layout.simple_spinner_item
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.projectgym.R.array
import com.example.projectgym.R.array.*
import com.google.firebase.auth.FirebaseAuth


class ProfileMenuFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile_menu, container, false)
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genderSpinner = view.findViewById<Spinner>(R.id.spinnerGender)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.SpinnerGender_items,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter

        val btnLogout = view.findViewById<Button>(R.id.btn_logout)
        btnLogout.setOnClickListener {
            Log.d("SignOut", "Start")
            try {
                auth.signOut()
            } catch (e: Exception) {
                Log.d("SignOut", "Exception : $e")
            }
            val mainActivity = Intent(activity, MainActivity::class.java)
            startActivity(mainActivity)
        }
    }

}