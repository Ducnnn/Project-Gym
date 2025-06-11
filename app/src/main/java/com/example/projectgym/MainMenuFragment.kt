package com.example.projectgym

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.Calendar
import java.util.Locale


class MainMenuFragment : Fragment() {
    private lateinit var attendanceCalendarView: CalendarView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    //setup pie chart
    private fun setupPieChart(pieChart: PieChart){
        val pieEntries = arrayListOf<PieEntry>()
        pieEntries.add(PieEntry(30.0f))
        pieEntries.add(PieEntry(40.0f))
        pieEntries.add(PieEntry(35.0f))
        //Setup animation
        pieChart.animateXY(1000,1000)
        //Setup colors
        val pieDataSet = PieDataSet(pieEntries,"Protein,Fats,Carbohydrates")
        pieDataSet.setColors(
            resources.getColor(R.color.Protein),
            resources.getColor(R.color.Fats),
            resources.getColor(R.color.Carbohydrates)
        )
        //Setup Pie Chart Data
        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(false)
        pieChart.data = pieData

        pieChart.legend.isEnabled = (false)
        pieChart.description.isEnabled = (false)



    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pieChart = view.findViewById<PieChart>(R.id.pieChart)
        setupPieChart(pieChart)

        val tvDateAndTrainingDay = view.findViewById<TextView>(R.id.date_and_training_day)

        val calendar = Calendar.getInstance().time
        val locale = Locale.getDefault()
        val dateFormat = SimpleDateFormat("MMM, dd", locale).format(calendar)
        tvDateAndTrainingDay.text = dateFormat
        val bottle = view.findViewById<ImageView>(R.id.imageView_water_bottle)
        val level = 7000
        bottle.background.setLevel(level)

        val btnProgram = view.findViewById<Button>(R.id.program_button)
        val btnCurDay = view.findViewById<Button>(R.id.list_of_exercises)
        val btnProfile = view.findViewById<Button>(R.id.profile_button)
        val mealsLayout = view.findViewById<ConstraintLayout>(R.id.chart_layout)

        btnProgram.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_trainingPlanMenuFragment)
        }
        btnCurDay.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_currentDayFragment)
        }
        btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_profileMenuFragment)
        }
        mealsLayout.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_mealsMenuFragment)
        }
        attendanceCalendarView = view.findViewById(R.id.calendarview_attendance)

        val currentYear = YearMonth.now().year
        val startMonth = YearMonth.of(currentYear, 1)
        val endMonth = YearMonth.of(currentYear, 12)


        val daysOfWeek = daysOfWeek()

        attendanceCalendarView.setup(startMonth, endMonth, daysOfWeek.first())
        attendanceCalendarView.scrollToMonth(YearMonth.now())
        attendanceCalendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {

            override fun create(view: View) = DayViewContainer(view)


            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data

                val dayView = container.dayView

                if (data.position == DayPosition.MonthDate) {
                    val bgDrawableRes = R.drawable.attendance_day_bg_default
                    dayView.background = ContextCompat.getDrawable(dayView.context, bgDrawableRes)
                    dayView.visibility = View.VISIBLE
                } else {
                    dayView.background = ContextCompat.getDrawable(
                        dayView.context,
                        R.drawable.attendance_day_bg_default
                    )
                }
            }
        }
    }

    class DayViewContainer(view: View) : ViewContainer(view) {
        val dayView: View = view.findViewById(R.id.dayView)
        lateinit var day: CalendarDay

        init {

            view.setOnClickListener {

            }
        }
    }
}

