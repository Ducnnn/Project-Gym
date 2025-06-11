package com.example.projectgym

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale


class MainMenuFragment : Fragment() {
    private lateinit var attendanceCalendarView: CalendarView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {}
        callback.isEnabled
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        val titlesContainer = view.findViewById<ViewGroup>(R.id.day_names_container)
        val daysOfWeek = daysOfWeek()
        titlesContainer.children
            .map { it as TextView }
            .forEachIndexed { index, textView ->
                val dayOfWeek = daysOfWeek[index]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
            }
        attendanceCalendarView = view.findViewById(R.id.calendarview_attendance)

        val currentYear = YearMonth.now().year
        val startMonth = YearMonth.of(currentYear, 1)
        val endMonth = YearMonth.of(currentYear, 12)


        attendanceCalendarView.setup(startMonth, endMonth, daysOfWeek.first())
        attendanceCalendarView.scrollToMonth(YearMonth.now())
        attendanceCalendarView.dayBinder = AttendanceDayBinder(lifecycleScope, view)
    }


    class AttendanceDayBinder(private val scope: CoroutineScope, private val viewFragment: View) :
        MonthDayBinder<AttendanceDayBinder.AttendanceDayViewContainer> {
        override fun create(view: View) = AttendanceDayViewContainer(view)

        override fun bind(container: AttendanceDayViewContainer, data: CalendarDay) {
            container.M_job = scope.launch {
                val trainingDay =
                    DatabaseInteractions().getTrainingDay(
                        WeekDay(
                            data.date,
                            WeekDayPosition.InDate
                        )
                    )
                val percentage : Int
                var finishedExercises = 0
                for (exercise in trainingDay.exercises) {
                    if (exercise.isCompleted) {
                        finishedExercises++
                    }
                }
                percentage = when {

                    trainingDay.exercises.isEmpty() -> -1
                    data.date.isAfter(LocalDate.now()) -> 101
                    else -> (finishedExercises.toDouble() / trainingDay.exercises.size * 100).toInt()
                }

                val bgDrawableRes = when (percentage) {
                    -1 -> R.drawable.attendance_day_bg_default
                    in 1..25 -> R.drawable.attendance_day_bg_level_1
                    in 25..50 -> R.drawable.attendance_day_bg_level_2
                    in 51..75 -> R.drawable.attendance_day_bg_level_3
                    in 76..100 -> R.drawable.attendance_day_bg_level_4
                    101 -> R.drawable.attendance_day_bg_level_planned
                    else -> R.drawable.attendance_day_bg_level_0
                }

                container.dayView.setBackgroundResource(bgDrawableRes)

                container.dayView.setOnClickListener {
                    showDayInfoDialog(viewFragment, trainingDay)
                }
            }
        }

        private fun showDayInfoDialog(view: View, day: TranDay) {
            val builder = AlertDialog.Builder(view.context)
            val inflater = LayoutInflater.from(view.context)
            val dialogView = inflater.inflate(R.layout.dialog_training_info, null)

            val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerview_days_info)

            recyclerView.layoutManager = LinearLayoutManager(view.context)
            val dayInfoAdapter = DayInfoAdapter(day.exercises)
            recyclerView.adapter = dayInfoAdapter
            val dayName = dialogView.findViewById<TextView>(R.id.tv_day_info_name)
            dayName.text = day.name
            builder.setView(dialogView).create().show()

        }

        open class AttendanceDayViewContainer(view: View) : ViewContainer(view) {
            val dayView: View = view.findViewById(R.id.dayView)
            var M_job: Job? = null

            init {
                view.setOnLongClickListener {

                    true
                }
            }
        }
    }
}

