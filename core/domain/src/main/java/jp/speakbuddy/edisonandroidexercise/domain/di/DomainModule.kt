package jp.speakbuddy.edisonandroidexercise.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jp.speakbuddy.edisonandroidexercise.data.repository.FactCatRepository
import jp.speakbuddy.edisonandroidexercise.data.repository.FactCatRepositoryImpl
import jp.speakbuddy.edisonandroidexercise.domain.usecase.FactCatUseCase
import jp.speakbuddy.edisonandroidexercise.domain.usecase.FactCatUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
internal interface DomainModule {
    @Binds
    fun bindFactCatUseCase(
        factCatUseCaseImpl: FactCatUseCaseImpl
    ): FactCatUseCase
}
