package org.example.project.features.common.data.database

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.project.WorkoutTrackerAppDb
import org.example.project.dbFactory.DatabaseFactory

class DbHelper(
    private val driverFactory : DatabaseFactory
) {
    private var db: WorkoutTrackerAppDb?= null
    private val mutex = Mutex()

    suspend fun  <Result: Any> withDatabase(block: suspend (WorkoutTrackerAppDb) -> Result) = mutex.withLock{
        if(db == null) {
            db = createDb(driverFactory)
        }
        return@withLock block(db!!)
    }

    private suspend fun createDb(driverFactory: DatabaseFactory): WorkoutTrackerAppDb {
        return WorkoutTrackerAppDb(
            driver = driverFactory.createDriver(),

        )
    }
}