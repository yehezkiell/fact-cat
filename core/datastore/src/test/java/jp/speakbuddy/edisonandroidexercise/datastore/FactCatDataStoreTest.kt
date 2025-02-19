package jp.speakbuddy.edisonandroidexercise.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FactCatDataStoreTest {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var factCatDataStore: FactCatDataStore
    private val preferences: Preferences = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        dataStore = mockk()
        factCatDataStore = FactCatDataStore(dataStore)
    }

    @Test
    fun `saveFact should store fact in preferences`() = runTest {
        // Given
        val testFact = "Cats sleep 70% of their lives"

        coEvery {
            dataStore.updateData(any())
        } returns preferences

        // When
        factCatDataStore.saveFact(testFact)

        // Then
        coVerify {
            dataStore.updateData(any())
        }
    }

    @Test
    fun `getFact should return stored fact`() = runTest {
        // Given
        val testFact = "Cats sleep 70% of their lives"
        val factKey = stringPreferencesKey("fact")

        every { preferences[factKey] } returns testFact
        every { dataStore.data } returns flowOf(preferences)

        // When
        val result = factCatDataStore.getFact().first()

        // Then
        assertEquals(testFact, result)
    }

    @Test
    fun `getFact should return null when no fact is stored`() = runTest {
        // Given
        val factKey = stringPreferencesKey("fact")

        every { preferences[factKey] } returns null
        every { dataStore.data } returns flowOf(preferences)

        // When
        val result = factCatDataStore.getFact().first()

        // Then
        assertEquals(null, result)
    }
}