package jp.speakbuddy.edisonandroidexercise.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import jp.speakbuddy.edisonandroidexercise.data.repository.FactCatRepository
import jp.speakbuddy.edisonandroidexercise.data.repository.FactCatRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
internal interface DataModule {
    @Binds
    fun bindFactCatRepository(
        factCatRepository: FactCatRepositoryImpl
    ): FactCatRepository
}
