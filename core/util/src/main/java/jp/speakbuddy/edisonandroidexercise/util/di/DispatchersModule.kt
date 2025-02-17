package jp.speakbuddy.edisonandroidexercise.util.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.speakbuddy.edisonandroidexercise.util.dispatchers.CoroutineDispatchers
import jp.speakbuddy.edisonandroidexercise.util.dispatchers.CoroutineDispatchersProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DispatchersModule {
    @Singleton
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }
}