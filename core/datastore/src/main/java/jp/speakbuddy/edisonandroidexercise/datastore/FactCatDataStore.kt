package jp.speakbuddy.edisonandroidexercise.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("fact")

@Singleton
class FactCatDataStore @Inject constructor(
    @ApplicationContext appContext: Context
) {
    private companion object {
        val FACT_KEY = stringPreferencesKey("fact")
    }

    private val dataStore by lazy {
        appContext.dataStore
    }

    suspend fun saveFact(fact: String) {
        try {
            dataStore.edit { preferences ->
                preferences[FACT_KEY] = fact
            }
        } catch (e:Throwable) {
            println(e)
        }
    }

    fun getFact(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[FACT_KEY]
        }
    }
}