package jp.speakbuddy.edisonandroidexercise.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import jp.speakbuddy.edisonandroidexercise.network.FactCatNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.network.FactCatNetworkDataSourceImpl

@Module
@InstallIn(ViewModelComponent::class)
internal interface NetworkDataSourceModule {
    @Binds
    fun bindFactCatNetworkDataSource(
        networkDataSource: FactCatNetworkDataSourceImpl
    ): FactCatNetworkDataSource
}
