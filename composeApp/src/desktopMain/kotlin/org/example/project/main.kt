package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.project.di.initKoinJvm

val koin = initKoinJvm()

fun main () = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "WorkoutTracker",
    ){
        App()
    }
}