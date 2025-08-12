package org.example.project.features.common.data.database

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.project.WokroutTrackerAppDb
import org.example.project.Workout
import org.example.project.WorkoutDetail
import org.example.project.dbFactory.DatabaseFactory

class DbHelper(
    private val driverFactory : DatabaseFactory
) {
    private var db: WokroutTrackerAppDb?= null
    private val mutex = Mutex()

    suspend fun  <Result: Any?> withDatabase(block: suspend (WokroutTrackerAppDb) -> Result) = mutex.withLock{
        if(db == null) {
            db = createDb(driverFactory)
        }
        return@withLock block(db!!)
    }

    private suspend fun createDb(driverFactory: DatabaseFactory): WokroutTrackerAppDb {
        return WokroutTrackerAppDb(
            driver = driverFactory.createDriver(),
            WorkoutAdapter = Workout.Adapter(
                bodyPartsAdapter = listOfStringsAdapter,
                equipmentsAdapter = listOfStringsAdapter,
                targetMusclesAdapter = listOfStringsAdapter,
                secondaryMusclesAdapter = listOfStringsAdapter,
                keywordsAdapter = listOfStringsAdapter,
            ),
            WorkoutDetailAdapter = WorkoutDetail.Adapter(
                bodyPartsAdapter = listOfStringsAdapter,
                equipmentsAdapter = listOfStringsAdapter,
                targetMusclesAdapter = listOfStringsAdapter,
                secondaryMusclesAdapter = listOfStringsAdapter,
                keywordsAdapter = listOfStringsAdapter,
                instructionsAdapter = listOfStringsAdapter,
                exerciseTipsAdapter = listOfStringsAdapter,
                variationsAdapter = listOfStringsAdapter,
                relatedExerciseIdsAdapter = listOfStringsAdapter
            )
        )
    }
}