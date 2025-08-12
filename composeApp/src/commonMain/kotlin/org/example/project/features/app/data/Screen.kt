package org.example.project.features.app.data

import org.example.project.features.detail.navigation.WORKOUT_ID_ARG
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import workouttracker.composeapp.generated.resources.Res
import workouttracker.composeapp.generated.resources.bookmark_selected
import workouttracker.composeapp.generated.resources.bookmark_unselected
import workouttracker.composeapp.generated.resources.details
import workouttracker.composeapp.generated.resources.dumbbell_selected
import workouttracker.composeapp.generated.resources.dumbbell_unselected
import workouttracker.composeapp.generated.resources.exercises
import workouttracker.composeapp.generated.resources.favorites
import workouttracker.composeapp.generated.resources.home
import workouttracker.composeapp.generated.resources.home_selected
import workouttracker.composeapp.generated.resources.home_unselected
import workouttracker.composeapp.generated.resources.profile
import workouttracker.composeapp.generated.resources.profile_selected
import workouttracker.composeapp.generated.resources.profile_unselected
import workouttracker.composeapp.generated.resources.search
import workouttracker.composeapp.generated.resources.tabs

sealed class Screen(
    val route: String,
    val resourceId: StringResource,
    val selectedIcon: DrawableResource? = null,
    val unselectedIcon: DrawableResource? = null
) {
    data object Search : Screen("search", Res.string.search)
    data object Tabs : Screen("tabs", Res.string.tabs)
    data object Detail : Screen("detail?$WORKOUT_ID_ARG={$WORKOUT_ID_ARG}", Res.string.details)


    data object Home : Screen("home",
        Res.string.home,
        selectedIcon = Res.drawable.home_selected,
        unselectedIcon = Res.drawable.home_unselected
    )

    data object Exercises : Screen("exercises",
        Res.string.exercises,
        selectedIcon = Res.drawable.dumbbell_selected,
        unselectedIcon = Res.drawable.dumbbell_unselected
    )

    data object Favorites : Screen("favorites",
        Res.string.favorites,
        selectedIcon = Res.drawable.bookmark_selected,
        unselectedIcon = Res.drawable.bookmark_unselected
    )

    data object Profile : Screen("profile",
        Res.string.profile,
        selectedIcon = Res.drawable.profile_selected,
        unselectedIcon = Res.drawable.profile_unselected
    )


}