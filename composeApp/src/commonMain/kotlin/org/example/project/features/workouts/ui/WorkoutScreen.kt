package org.example.project.features.workouts.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun WorkoutRoute(){
    Column {
        WorkoutScreen()
    }
}


@Composable
fun WorkoutScreen(){
    Text("WORKOUT SCREEN")
}