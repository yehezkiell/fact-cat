package jp.speakbuddy.edisonandroidexercise.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FactCatDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val FACT_KEY = stringPreferencesKey("fact")
    }

    suspend fun saveFact(fact: String) {
        try {
            dataStore.edit { preferences ->
                preferences[FACT_KEY] = fact
            }
        } catch (e: Throwable) {
            println(e)
        }
    }

    fun getFact(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[FACT_KEY]
        }
    }
}