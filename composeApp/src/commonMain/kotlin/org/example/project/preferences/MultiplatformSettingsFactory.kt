package org.example.project.preferences

import com.russhwolf.settings.Settings

expect class MultiplatformSettingsFactory {
    fun getSettings(): Settings
}