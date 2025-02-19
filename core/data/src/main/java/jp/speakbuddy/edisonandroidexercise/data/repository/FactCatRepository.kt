package jp.speakbuddy.edisonandroidexercise.data.repository

import jp.speakbuddy.edisonandroidexercise.data.model.FactCatDataModel
import jp.speakbuddy.edisonandroidexercise.data.model.toDataModel
import jp.speakbuddy.edisonandroidexercise.datastore.FactCatDataStore
import jp.speakbuddy.edisonandroidexercise.network.FactCatNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.util.dispatchers.CoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>
    data class Error<T>(val error: Throwable) : DataResult<T>
}

interface FactCatRepository {
    fun getFact(isFirstOpen: Boolean): Flow<DataResult<FactCatDataModel>>
}

class FactCatRepositoryImpl @Inject constructor(
    private val factCatNetworkDataSource: FactCatNetworkDataSource,
    private val factCatDataStore: FactCatDataStore,
    private val dispatchers: CoroutineDispatchers
) : FactCatRepository {

    override fun getFact(isFirstOpen: Boolean): Flow<DataResult<FactCatDataModel>> = flow {
        val localData = factCatDataStore.getFact().firstOrNull()
        if (localData != null && isFirstOpen) {
            emit(DataResult.Success(localData.toDataModel()))
        } else {
            val networkData = factCatNetworkDataSource.getFact()
            networkData.fold(
                onSuccess = { networkResult ->
                    factCatDataStore.saveFact(networkResult.fact)
                    emit(DataResult.Success(networkResult.toDataModel()))
                },
                onFailure = { throwable ->
                    emit(DataResult.Error<FactCatDataModel>(throwable))
                }
            )
        }
    }.catch {
        emit(DataResult.Error(it))
    }.flowOn(dispatchers.io)
}