package jp.speakbuddy.edisonandroidexercise.domain.usecase

import jp.speakbuddy.edisonandroidexercise.data.repository.DataResult
import jp.speakbuddy.edisonandroidexercise.data.repository.FactCatRepository
import jp.speakbuddy.edisonandroidexercise.domain.model.FactCatDomainModel
import jp.speakbuddy.edisonandroidexercise.domain.model.toDomainModel
import jp.speakbuddy.edisonandroidexercise.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface FactCatUseCase {
    fun getFact(isFirstOpen: Boolean): Flow<DomainResult<FactCatDomainModel>>
}

class FactCatUseCaseImpl @Inject constructor(private val factCatRepository: FactCatRepository) :
    FactCatUseCase {
    override fun getFact(isFirstOpen: Boolean): Flow<DomainResult<FactCatDomainModel>> =
        factCatRepository.getFact(isFirstOpen).map {
            when (it) {
                is DataResult.Success -> {
                    DomainResult.Success(it.data.toDomainModel())
                }

                is DataResult.Error -> {
                    DomainResult.Error(it.error.message ?: "Something went wrong!")
                }
            }
        }

}