package org.example.project.features.common.data.database.daos

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.WorkoutDetail
import org.example.project.features.common.data.database.DbHelper
import org.example.project.features.common.data.database.workoutDetailEntityMapper
import org.example.project.features.common.data.database.workoutEntityMapper
import org.example.project.features.common.domain.entities.WorkoutDetailItem
import org.example.project.features.common.domain.entities.WorkoutItem

class FavoriteWorkoutDao(
    private val dbHelper: DbHelper
) {
    suspend fun addFavorite(exerciseId: String) {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        dbHelper.withDatabase { database ->
            database.favoriteWorkoutQueries.upsertFavoriteWorkout(
                exerciseId = exerciseId,
                recipe_id = exerciseId,
                added_at = currentDateTime.toString()
            )
        }
    }

    suspend fun removeFavorite(exerciseId: String) {
        dbHelper.withDatabase { database ->
            database.favoriteWorkoutQueries.deleteFavoriteWorkoutById(
                recipe_id = exerciseId,
            )
        }
    }

    suspend fun getAllFavorites(): List<WorkoutDetailItem> {
        return dbHelper.withDatabase { database ->
            database.favoriteWorkoutQueries.selectAllFavoriteWorkouts().awaitAsList().map {
                workoutDetailEntityMapper(it)
            }
        }
    }

    suspend fun isFavorite(exerciseId: String): Boolean {
        return dbHelper.withDatabase { database ->
            database.favoriteWorkoutQueries.selectIsFavorite(exerciseId).awaitAsOneOrNull() != null
        }
    }
}