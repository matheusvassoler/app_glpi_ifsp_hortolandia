package com.glpi.ifsp.hortolandia.data.source.local

interface AppSharedPreferences {

    fun save(keyName: String, value: String)

    fun save(keyName: String, value: Boolean)

    fun getStringValue(keyName: String): String?

    fun getBooleanValue(keyName: String): Boolean
}
