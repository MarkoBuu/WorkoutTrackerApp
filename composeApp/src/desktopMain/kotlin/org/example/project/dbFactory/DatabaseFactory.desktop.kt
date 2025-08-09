package org.example.project.dbFactory

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.example.project.WorkoutTrackerAppDb

actual class DatabaseFactory {
    actual suspend fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver(
            JdbcSqliteDriver.IN_MEMORY
        )
        WorkoutTrackerAppDb.Schema.awaitCreate(driver)
        return driver
    }
}