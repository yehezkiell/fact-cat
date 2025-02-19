package jp.speakbuddy.edisonandroidexercise.data.repository

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import jp.speakbuddy.edisonandroidexercise.data.model.FactCatDataModel
import jp.speakbuddy.edisonandroidexercise.datastore.FactCatDataStore
import jp.speakbuddy.edisonandroidexercise.network.FactCatNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.network.service.FactResponse
import jp.speakbuddy.edisonandroidexercise.testing.dispatchers.CoroutineTestDispatchersProvider
import jp.speakbuddy.edisonandroidexercise.util.dispatchers.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FactCatRepositoryTest {

    private lateinit var repository: FactCatRepository

    @RelaxedMockK
    private lateinit var networkDataSource: FactCatNetworkDataSource

    @RelaxedMockK
    private lateinit var dataStore: FactCatDataStore

    @Before
    fun beforeTest() {
        MockKAnnotations.init(this)
        repository = FactCatRepositoryImpl(
            networkDataSource,
            dataStore,
            CoroutineTestDispatchersProvider
        )
    }

    private val mockFactResponse
        get() = FactResponse(
            fact = "fact 1",
            length = 10
        )

    @Test
    fun getFactRepository_whenFirstOpenAndLocalDataExists_shouldReturnLocalData() = runTest {
        // Given
        val localFact = "Local fact"
        coEvery {
            dataStore.getFact()
        } returns flowOf(localFact)

        // When
        val result = repository.getFact(isFirstOpen = true).first()

        // Then
        Assert.assertTrue(result is DataResult.Success)
        Assert.assertEquals(localFact, (result as DataResult.Success).data.fact)
        coVerify(exactly = 0) { networkDataSource.getFact() }
    }

    @Test
    fun getFactRepository_whenNotFirstOpen_shouldFetchFromNetwork() = runTest {
        // Given
        coEvery {
            dataStore.getFact()
        } returns flowOf(null)

        coEvery {
            networkDataSource.getFact()
        } returns Result.success(mockFactResponse)

        // When
        val result = repository.getFact(isFirstOpen = false).first()

        // Then
        Assert.assertTrue(result is DataResult.Success)
        Assert.assertEquals(mockFactResponse.fact, (result as DataResult.Success).data.fact)
        coVerify { networkDataSource.getFact() }
        coVerify { dataStore.saveFact(mockFactResponse.fact) }
    }

    @Test
    fun getFactRepository_whenNetworkError_shouldReturnError() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery {
            dataStore.getFact()
        } returns flowOf(null)

        coEvery {
            networkDataSource.getFact()
        } returns Result.failure(Throwable(errorMessage))

        // When
        val result = repository.getFact(isFirstOpen = false).first()

        // Then
        Assert.assertTrue(result is DataResult.Error)
        Assert.assertEquals(errorMessage, (result as DataResult.Error).error.message)
    }

    @Test
    fun getFactRepository_whenNoLocalDataAndFirstOpen_shouldFetchFromNetwork() = runTest {
        // Given
        coEvery {
            dataStore.getFact()
        } returns flowOf(null)

        coEvery {
            networkDataSource.getFact()
        } returns Result.success(mockFactResponse)

        // When
        val result = repository.getFact(isFirstOpen = true).first()

        // Then
        Assert.assertTrue(result is DataResult.Success)
        Assert.assertEquals(mockFactResponse.fact, (result as DataResult.Success).data.fact)
        coVerify { networkDataSource.getFact() }
        coVerify { dataStore.saveFact(mockFactResponse.fact) }
    }

    @Test
    fun getFactRepository_whenDataStoreThrowsException_shouldCatchAndReturnError() = runTest {
        // Given
        val errorMessage = "DataStore error"
        coEvery { 
            dataStore.getFact() 
        } throws RuntimeException(errorMessage)

        // When
        val result = repository.getFact(isFirstOpen = true).first()

        // Then
        Assert.assertTrue(result is DataResult.Error)
        Assert.assertEquals(errorMessage, (result as DataResult.Error).error.message)
        coVerify(exactly = 0) { networkDataSource.getFact() }  // Network should not be called
    }
} 