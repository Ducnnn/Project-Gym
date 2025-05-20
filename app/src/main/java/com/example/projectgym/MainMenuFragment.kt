package com.example.projectgym

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.datetime.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainMenuFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val txt = view.findViewById<TextView>(R.id.txt_day)

        val calendar = Calendar.getInstance().time
        val locale = Locale.getDefault() //
        val dateFormat = SimpleDateFormat("MMM, dd", locale).format(calendar)
        txt.text = dateFormat
    }
}