package kr.minjae.develop.jjiggijjiggi.di

import android.util.Log
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kr.minjae.develop.jjiggijjiggi.network.service.OcrService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://1rmli9fbnp.apigw.ntruss.com/custom/v1/19295/8ad13c0581164d1e8d431ce165e49cad2132ac13a5eed27bae6825c4f10a1ab9/"

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okhttpBuilder = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
        return okhttpBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .build()
    }

    @Provides
    @Singleton
    fun provideOcrService(retrofit: Retrofit): OcrService =
        retrofit.create(OcrService::class.java)

}