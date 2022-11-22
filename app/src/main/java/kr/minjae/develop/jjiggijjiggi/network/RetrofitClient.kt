package kr.minjae.develop.jjiggijjiggi.network

import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit
import kr.minjae.develop.jjiggijjiggi.network.service.OcrService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://1rmli9fbnp.apigw.ntruss.com/custom/v1/19295/8ad13c0581164d1e8d431ce165e49cad2132ac13a5eed27bae6825c4f10a1ab9/"

    val ocrService: OcrService

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val logger = OkHttpClient.Builder().addInterceptor(interceptor)
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .client(logger)
        .build()

    init {
        ocrService = retrofit.create(OcrService::class.java)
    }
}