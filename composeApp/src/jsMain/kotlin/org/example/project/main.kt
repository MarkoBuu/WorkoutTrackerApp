package org.example.project

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import kotlinx.browser.document
import org.example.project.di.initKoinJs

val koin = initKoinJs()

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    Firebase.initialize(
        options = FirebaseOptions(
            applicationId = "1:458885340009:web:47e2889eb3add860173bf8",
            apiKey = "AIzaSyD2ElMOQn3djBGj6qPvosF-hq3qoBWR6Oo",
            projectId = "workouttracker-28b07"
        )
    )

    ComposeViewport(document.body!!) {
        App()
    }
}