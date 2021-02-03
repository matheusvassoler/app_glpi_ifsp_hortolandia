package com.glpi.ifsp.hortolandia.data.source.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

class AppSharedPreferencesImpl(context: Context) : AppSharedPreferences {

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

    override fun save(keyName: String, value: String) {
        preferences.edit {
            putString(keyName, value)
        }
    }

    override fun save(keyName: String, value: Boolean) {
        preferences.edit {
            putBoolean(keyName, value)
        }
    }

    override fun getStringValue(keyName: String): String? =
        preferences.getString(keyName, null)

    override fun getBooleanValue(keyName: String): Boolean =
        preferences.getBoolean(keyName, false)

    companion object {
        private const val PREFS_NAME = "user_data"
    }
}
