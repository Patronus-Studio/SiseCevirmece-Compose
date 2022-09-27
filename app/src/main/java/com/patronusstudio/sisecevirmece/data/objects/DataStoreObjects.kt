package com.patronusstudio.sisecevirmece.data.objects

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object DataStoreObjects {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
}