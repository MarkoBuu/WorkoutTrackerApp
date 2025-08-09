package org.example.project.dbFactory

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.example.project.WorkoutTrackerAppDb

actual class DatabaseFactory {
    actual suspend fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            WorkoutTrackerAppDb.Schema.synchronous(), DB_FILE_NAME
        )
    }
}