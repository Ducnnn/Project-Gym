package com.example.projectgym

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import kotlinx.coroutines.launch
import java.time.LocalDate


class CurrentDayFragment : Fragment() {
    private lateinit var listOfExercises: MutableList<Exercise>
    private lateinit var dayOfWeek: WeekDay
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = LocalDate.now()
        dayOfWeek = WeekDay(date, WeekDayPosition.InDate)
        var currentDay: TranDay

        var currentDayRecyclerView: RecyclerView
        var currentDayAdapter: CurrentDayAdapter
        lifecycleScope.launch {
            currentDay = DatabaseInteractions().getTrainingDay(dayOfWeek)
            view.findViewById<TextView>(R.id.tv_current_day_name).also {
                it.text = currentDay.name
            }
            listOfExercises = currentDay.exercises
            currentDayAdapter = CurrentDayAdapter(listOfExercises)
            currentDayRecyclerView =
                view.findViewById<RecyclerView>(R.id.recyclerview_current_day).also {
                    it.layoutManager = LinearLayoutManager(view.context)
                    it.adapter = currentDayAdapter
                }
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (this::listOfExercises.isInitialized) {
            DatabaseInteractions().updateNumberOfSets(dayOfWeek, listOfExercises)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::listOfExercises.isInitialized) {
            DatabaseInteractions().updateNumberOfSets(dayOfWeek, listOfExercises)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (this::dayOfWeek.isInitialized) {
            lifecycleScope.launch {
                listOfExercises = DatabaseInteractions().getTrainingDay(dayOfWeek).exercises
            }
        }
    }
}