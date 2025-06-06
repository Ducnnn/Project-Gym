package com.example.projectgym

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainMenuFragment : Fragment() {
    private lateinit var imageViewWaterBottle: ImageView
    private var bottleLayerDrawable: LayerDrawable? = null
    private var currentWaterLevelPercentage: Int = 0
        private set // Makes the setter private, only changeable within this class

    private val isWaterBottleFull: Boolean // Read-only property
        get() = currentWaterLevelPercentage >= 100
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
        val level = 7000;   // pct goes from 0 to 100
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
        // --- Water Bottle Initialization and Interaction ---
        // 1. Find the ImageView for the water bottle
//        try { // Added try-catch for safety during initialization
//            imageViewWaterBottle = view.findViewById(R.id.imageView_water_bottle)
//
//            // 2. Get the LayerDrawable from the ImageView
//            val drawable = imageViewWaterBottle.drawable
//            if (drawable is LayerDrawable) {
//                bottleLayerDrawable = drawable
//                // 3. Set an initial water level (e.g., 0% or load from saved state)
//                setWaterLevelPercentage(0) // Start empty
//                Log.d("MainMenuFragment", "Water bottle initialized. Current level: $currentWaterLevelPercentage%")
//            } else {
//                Log.e("MainMenuFragment", "imageViewWaterBottle's drawable is NOT a LayerDrawable or is null. " +
//                        "ID found: ${true}, Drawable type: ${drawable?.javaClass?.simpleName}")
//            }
//        } catch (e: Exception) {
//            Log.e("MainMenuFragment", "Error finding or initializing imageViewWaterBottle (R.id.imageView_water_bottle): ${e.message}")
//            // Handle cases where the ImageView might not be present or other setup issues
//        }
//
//
//        // 4. Set up the click listener for mealsLayout to ALSO update water level
//        mealsLayout.setOnClickListener {
//            // Update water level (example: add 20%)
//            val newLevel = (currentWaterLevelPercentage + 20).coerceAtMost(100)
//            setWaterLevelPercentage(newLevel)
//
//            if (isWaterBottleFull) {
//                Log.d("MainMenuFragment", "mealsLayout Clicked: Water bottle is now full!")
//            } else {
//                Log.d("MainMenuFragment", "mealsLayout Clicked: Water bottle level: $currentWaterLevelPercentage%")
//            }
//
//            // Still navigate to meals if that's the desired behavior
//            findNavController().navigate(R.id.action_mainMenuFragment_to_mealsMenuFragment)
//        }
        // --- End Water Bottle Initialization and Interaction ---
    }

    // --- Water Bottle Logic Function ---
    /**
     * Sets the water level of the bottle visually and updates the internal state.
     * @param newPercentage The desired fill percentage (0 to 100).
     */
    private fun setWaterLevelPercentage(newPercentage: Int) {
        // Ensure the percentage is within the valid range (0-100)
        val constrainedPercentage = newPercentage.coerceIn(0, 100)
        currentWaterLevelPercentage = constrainedPercentage // Update our state variable

        // Only proceed if bottleLayerDrawable has been initialized
        bottleLayerDrawable?.let { currentBottleDrawable ->
            // Convert the percentage (0-100) to the Drawable's level scale (0-10000)
            val drawableLevel = currentWaterLevelPercentage * 100

            // Ensure the LayerDrawable has the expected number of layers (at least two)
            if (currentBottleDrawable.numberOfLayers > 1) {
                // Get the second layer (index 1), which should be your ClipDrawable
                val clipDrawable = currentBottleDrawable.getDrawable(1)

                // Set the level on the ClipDrawable. This makes it "reveal" the underlying image.
                clipDrawable.level = drawableLevel
                // Log.d("MainMenuFragment", "Set water to: $currentWaterLevelPercentage%, Drawable level: $drawableLevel") // Optional detailed log
            } else {
                Log.e("MainMenuFragment", "LayerDrawable does not have enough layers (expected 2) for water level. Layers found: ${currentBottleDrawable.numberOfLayers}")
            }
        } ?: run {
            // This block runs if bottleLayerDrawable is null
            if (!::imageViewWaterBottle.isInitialized || imageViewWaterBottle.drawable !is LayerDrawable) {
                Log.e("MainMenuFragment", "setWaterLevelPercentage called but bottleLayerDrawable is null. ImageView or its drawable not set up correctly.")
            } // Closes the 'if' statement
        } // Closes the 'run' block
    } // Closes the 'setWaterLevelPercentage' function

}