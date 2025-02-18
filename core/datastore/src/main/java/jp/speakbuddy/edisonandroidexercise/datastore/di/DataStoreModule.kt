package jp.speakbuddy.edisonandroidexercise.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.speakbuddy.edisonandroidexercise.util.dispatchers.CoroutineDispatchers
import jp.speakbuddy.edisonandroidexercise.util.dispatchers.CoroutineDispatchersProvider
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.prefs.Preferences
import javax.inject.Singleton

private const val FACT_PREFERENCES = "fact_preferences"

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

    @Singleton
    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context,
        coroutinesDispatcher: CoroutineDispatchers
    ): DataStore<androidx.datastore.preferences.core.Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(appContext, FACT_PREFERENCES)),
            scope = CoroutineScope(coroutinesDispatcher.io + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(FACT_PREFERENCES) }
        )
    }
}