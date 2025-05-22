package com.example.projectgym

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class TrainingPlanMenuFragment : Fragment() {


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
        val chestDay = TranDay("Chest")
        val backDay = TranDay("Back")
        val legsDay = TranDay("Legs")
        val lst = mutableListOf(chestDay, backDay, legsDay)

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

    }


}