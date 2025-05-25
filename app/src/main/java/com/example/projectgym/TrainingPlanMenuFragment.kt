package com.example.projectgym

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import androidx.core.graphics.toColorInt
import androidx.core.view.get
import com.example.projectgym.TrainingPlanAdapter.OnLongClickListener
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import com.kizitonwose.calendar.view.WeekHeaderFooterBinder
import kotlinx.datetime.DayOfWeek
import org.w3c.dom.Text
import java.time.LocalDate
import kotlin.time.Duration.Companion.days


class TrainingPlanMenuFragment : Fragment() {
    private lateinit var calendarView: WeekCalendarView
    private lateinit var calendarViewBinder: WeekWorkoutToDayBinder
    private val chestDay = TranDay("Chest", color = "#2fe2f1")
    private val backDay = TranDay("Back", color = "#7902a0")
    private val legsDay = TranDay("Legs", color = "#07e0ab")
    private val lst = mutableListOf(chestDay, backDay, legsDay)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_training_plan_menu, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trainingPlanAdapter = TrainingPlanAdapter(lst)
        trainingPlanAdapter.setOnLongClickListener(object :
            TrainingPlanAdapter.OnLongClickListener {
            override fun onLongClick(position: Int) {
                AlertDialog.Builder(view.context)
                    .setMessage("Are you sure you want to delete this day?  ")
                    .setTitle("Deletion of a Day")
                    .setPositiveButton("YES") { _, _ ->
                        lst.removeAt(position)
                        trainingPlanAdapter.notifyItemRemoved(position)
                        trainingPlanAdapter.notifyItemRangeChanged(position, lst.size)
                    }
                    .setNegativeButton("NO") { _, _ -> }
                    .create().show()
            }
        })
        val trainingPlanRecyclerView =
            view.findViewById<RecyclerView>(R.id.recyclerview_training_plan)
        trainingPlanRecyclerView.layoutManager = LinearLayoutManager(view.context)
        trainingPlanRecyclerView.adapter = trainingPlanAdapter

        calendarView = view.findViewById(R.id.calendarView_training_plan)

        calendarViewBinder = WeekWorkoutToDayBinder()
        calendarViewBinder.setClickListener(object: WeekWorkoutToDayBinder.OnClickListener{
            override fun onClick(data: WeekDay) {
                selectDayForDate(data)
            }
        })
        calendarView.dayBinder = calendarViewBinder
        val currentDate = LocalDate.now()
        val currentMonth = YearMonth.now()
        val startDate = currentMonth.minusMonths(100).atStartOfMonth()
        val endDate = currentMonth.plusMonths(100).atEndOfMonth()
        calendarView.setup(startDate, endDate, DayOfWeek.MONDAY)
        calendarView.scrollToWeek(currentDate)
        calendarView.weekHeaderBinder =
            object : WeekHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: Week) {
                    val month = YearMonth.from(data.days.first().date).month.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    )
                    (container.titlesContainer[0] as TextView).text = month
                    (container.titlesContainer[1] as ViewGroup).children
                        .map { it as TextView }
                        .forEachIndexed { index, textView ->
                            val dayOfWeek = data.days[index].date.dayOfWeek
                            val title =
                                dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            textView.text = title
                            textView.setTextColor("#FFFFFF".toColorInt())
                        }
                }
            }
    }

    class DayViewContainer(view: View) : ViewContainer(view) {
        val textView = view.findViewById<CircularTextView>(R.id.calendarDayText)
    }

    class MonthViewContainer(view: View) : ViewContainer(view) {
        val titlesContainer = view as ViewGroup
    }

    private fun selectDayForDate(data : WeekDay) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose Day Option")

        builder.setPositiveButton("Pick from existing") { dialogInterface, _ ->
            showExistingDaysPickerDialog(data)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("Custom Day") { dialogInterface, _ ->
            Toast.makeText(requireContext(), "Custom Day clicked", Toast.LENGTH_SHORT).show()
            // TODO: Implement logic for "Custom Day"
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun showExistingDaysPickerDialog(data : WeekDay){
        if (lst.isEmpty()) {
            Toast.makeText(requireContext(), "No existing days found.", Toast.LENGTH_SHORT).show()
        }
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pick an Existing Day")
        val dayNames = mutableListOf<String>()
        for (day in lst) {
            dayNames.add(day.name)
        }
        val itemsAsCharSequenceArray = dayNames.toTypedArray<CharSequence>()
        builder.setItems(itemsAsCharSequenceArray) { dialogInterface, which ->
            val selectedDay = lst[which]
            calendarViewBinder.setWorkoutToDay(data, selectedDay)
            calendarView.notifyDayChanged(data)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val itemPickerDialog: AlertDialog = builder.create()
        itemPickerDialog.show()
    }

    class WeekWorkoutToDayBinder(
        private val mapDayToWorkout: MutableMap<WeekDay, TranDay> = mutableMapOf()
    ) : WeekDayBinder<DayViewContainer> {
        private var onClickListener: OnClickListener? = null
        override fun bind(container: DayViewContainer, data: WeekDay) {
            if(!mapDayToWorkout.containsKey(data)){
                mapDayToWorkout[data] = TranDay(name = "Rest", color="#ffa9a3")
            }
            container.textView.setStrokeColor(mapDayToWorkout[data]?.color)
            container.textView.setSolidColor("#FFFFFF")
            container.textView.setStrokeWidth(6)
            container.textView.text = data.date.dayOfMonth.toString()
            container.view.setOnClickListener {
                onClickListener?.onClick(data)
            }
        }

        fun setWorkoutToDay(day: WeekDay, trainingDay: TranDay) {
            mapDayToWorkout[day] = trainingDay
        }

        override fun create(view: View): DayViewContainer = DayViewContainer(view)
        fun setClickListener(listener: OnClickListener?) {
            this.onClickListener = listener
        }

        interface OnClickListener {
            fun onClick(data: WeekDay)
        }

    }
}
