package com.posart.klima

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val UNIT_SYSTEM = stringPreferencesKey("unit_system")

enum class UnitSystem(val value: String) {
    METRIC("metric"),
    IMPERIAL("imperial")
}