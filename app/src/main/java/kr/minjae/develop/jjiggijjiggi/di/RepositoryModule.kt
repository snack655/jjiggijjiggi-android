package kr.minjae.develop.jjiggijjiggi.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kr.minjae.develop.jjiggijjiggi.network.repository.ocr.OcrRepository
import kr.minjae.develop.jjiggijjiggi.network.repository.ocr.OcrRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun providesOcrRepository(
        ocrRepositoryImpl: OcrRepositoryImpl
    ): OcrRepository

}