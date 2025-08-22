package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        Firebase.initialize(
            applicationContext,
            options = FirebaseOptions(
                applicationId = "1:458885340009:android:942d66588763b4b9173bf8",
                apiKey = "AIzaSyD2ElMOQn3djBGj6qPvosF-hq3qoBWR6Oo",
                projectId = "workouttracker-28b07"
            )
        )
        setContent {
            App()
        }
    }
}
