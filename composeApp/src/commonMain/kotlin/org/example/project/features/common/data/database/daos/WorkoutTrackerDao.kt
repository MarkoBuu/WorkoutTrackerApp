package org.example.project.features.common.data.database.daos

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import org.example.project.features.common.data.database.DbHelper
import org.example.project.features.common.data.database.workoutDetailEntityMapper
import org.example.project.features.common.data.database.workoutEntityMapper
import org.example.project.features.common.domain.entities.WorkoutDetailItem
import org.example.project.features.common.domain.entities.WorkoutItem

class WorkoutTrackerDao(
    private val dbHelper: DbHelper
) {
    suspend fun insertWorkout(workoutItem : WorkoutItem){
        dbHelper.withDatabase {  database ->
            database.workoutTrackerEntityQueries.insertWorkout(
                workoutItem.exerciseId,
                workoutItem.name,
                workoutItem.imageUrl,
                workoutItem.exerciseType,
                workoutItem.bodyParts,
                workoutItem.equipments,
                workoutItem.targetMuscles,
                workoutItem.secondaryMuscles,
                workoutItem.keywords,
                if(workoutItem.isFavorite) 1 else 0
            )
        }
    }

    suspend fun insertWorkoutDetail(workoutItem : WorkoutDetailItem){
        dbHelper.withDatabase {  database ->
            database.workoutTrackerEntityQueries.insertWorkoutDetail(
                workoutItem.exerciseId,
                workoutItem.name,
                workoutItem.imageUrl,
                workoutItem.exerciseType,
                workoutItem.bodyParts,
                workoutItem.equipments,
                workoutItem.targetMuscles,
                workoutItem.secondaryMuscles,
                workoutItem.keywords,
                workoutItem.overview,
                workoutItem.instructions,
                workoutItem.exerciseTips,
                workoutItem.variations,
                workoutItem.relatedExerciseIds,
                workoutItem.videoUrl
            )
        }
    }

    suspend fun updateWorkout(workoutItem : WorkoutItem){
        dbHelper.withDatabase {  database ->
            database.workoutTrackerEntityQueries.updateWorkout(
                workoutItem.name,
                workoutItem.imageUrl,
                workoutItem.exerciseType,
                workoutItem.bodyParts,
                workoutItem.equipments,
                workoutItem.targetMuscles,
                workoutItem.secondaryMuscles,
                workoutItem.keywords,
                if(workoutItem.isFavorite) 1 else 0,
                workoutItem.exerciseId
            )
        }
    }

    suspend fun updateWorkoutDetail(workoutItem : WorkoutDetailItem){
        dbHelper.withDatabase {  database ->
            database.workoutTrackerEntityQueries.updateWorkoutDetail(
                workoutItem.name,
                workoutItem.imageUrl,
                workoutItem.exerciseType,
                workoutItem.bodyParts,
                workoutItem.equipments,
                workoutItem.targetMuscles,
                workoutItem.secondaryMuscles,
                workoutItem.keywords,
                workoutItem.overview,
                workoutItem.instructions,
                workoutItem.exerciseTips,
                workoutItem.variations,
                workoutItem.relatedExerciseIds,
                workoutItem.videoUrl,
                workoutItem.exerciseId
            )
        }
    }

    suspend fun insertWorkoutsBulk(workouts: List<WorkoutItem>) {
        dbHelper.withDatabase { database ->
            workouts.forEach { workoutItem ->
                database.workoutTrackerEntityQueries.insertWorkout(
                    workoutItem.exerciseId,
                    workoutItem.name,
                    workoutItem.imageUrl,
                    workoutItem.exerciseType,
                    workoutItem.bodyParts,
                    workoutItem.equipments,
                    workoutItem.targetMuscles,
                    workoutItem.secondaryMuscles,
                    workoutItem.keywords,
                    if (workoutItem.isFavorite) 1 else 0
                )
            }
        }
    }

    suspend fun upsertWorkoutsBulk(workouts: List<WorkoutItem>) {
        dbHelper.withDatabase { database ->
            workouts.forEach { workoutItem ->
                database.workoutTrackerEntityQueries.updateWorkout(
                    workoutItem.name,
                    workoutItem.imageUrl,
                    workoutItem.exerciseType,
                    workoutItem.bodyParts,
                    workoutItem.equipments,
                    workoutItem.targetMuscles,
                    workoutItem.secondaryMuscles,
                    workoutItem.keywords,
                    if (workoutItem.isFavorite) 1 else 0,
                    workoutItem.exerciseId
                )
            }
        }
    }

    suspend fun getAllWorkouts() : List<WorkoutItem> {
        return dbHelper.withDatabase { database ->
            database.workoutTrackerEntityQueries.selectAllWorkouts().awaitAsList().map {
                workoutEntityMapper(it)
            }
        }
    }

    suspend fun getWorkoutById(exerciseId: String) : WorkoutItem? {
        return dbHelper.withDatabase { database ->
            database.workoutTrackerEntityQueries.selectWorkoutById(exerciseId).awaitAsOneOrNull()?.let {
                workoutEntityMapper(it)
            }
        }
    }

    suspend fun getWorkoutDetailById(exerciseId: String) : WorkoutDetailItem? {
        return dbHelper.withDatabase { database ->
            database.workoutTrackerEntityQueries.selectWorkoutDetailById(exerciseId).awaitAsOneOrNull()?.let {
                workoutDetailEntityMapper(it)
            }
        }
    }

    suspend fun deleteWorkoutById(exerciseId: String){
         dbHelper.withDatabase { database ->
            database.workoutTrackerEntityQueries.deleteWorkoutById(exerciseId)
        }
    }

}