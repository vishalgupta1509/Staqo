package com.staqoapp.network

import android.util.Log
import com.staqoapp.BuildConfig
import com.staqoapp.utils.ErrorBean
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    factory { AuthInterceptor() }
    factory { provideOkHttpClient(get()) }
    factory { apiInterface(get()) }
    single { provideRetrofit(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

    return Retrofit.Builder()
        .addCallAdapterFactory(LiveDataCallAdapterFactory())                                    // LiveDataCall adapter.
//        .addCallAdapterFactory(CoroutineCallAdapterFactory())                                   // Kotlin coroutines adapter.
        //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())                              // RxJava Adapter
        .addConverterFactory(GsonConverterFactory.create())                                     // GSON builder
        .addConverterFactory(ScalarsConverterFactory.create())                                  // Scalars converter
        .client(okHttpClient)
        .baseUrl(BuildConfig.BASE_URL)
        .build()
}

fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    val okHttpClientBuilder = OkHttpClient.Builder()

    val loggingInterceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        loggingInterceptor.level =
            HttpLoggingInterceptor.Level.BODY                                // Logging interceptor
        okHttpClientBuilder.addInterceptor(loggingInterceptor)
    }
//    okHttpClientBuilder.addInterceptor(authInterceptor)
    okHttpClientBuilder.connectTimeout(
        30,
        TimeUnit.SECONDS
    )                                // connectTimeout
    okHttpClientBuilder.readTimeout(
        50,
        TimeUnit.SECONDS
    )                                   // readTimeout
    okHttpClientBuilder.writeTimeout(
        60,
        TimeUnit.SECONDS
    )                                  // readTimeout

    return okHttpClientBuilder.build()
}

fun apiInterface(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)


class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val tag = "**Interceptor**"
        val request = chain.request()
        val connection = chain.connection()
        val originalResponse = chain.proceed(request)

        val requestBody = originalResponse.body

        val bodyString = requestBody?.string()
        val contentType = requestBody?.contentType()

        val error = Gson().fromJson(bodyString, ErrorBean::class.java)
        val statusCode = error.code

        Log.d("OK HTTP ", "ERROR CLASS OK HTTP : -> " + originalResponse)
        Log.d("OK HTTP ", "error class code -> " + error.code + "  message " + error.message)

        // PRINTING REQUEST AND RESPONSE LOGS
        if (BuildConfig.DEBUG) {
            var requestStartMessage =
                ("--> ${request.method} ${request.url}${if (connection != null) " " + connection.protocol() else ""}")
            if (requestBody != null) {
                requestStartMessage += " (${requestBody.contentLength()}-byte body)"
            }
            Log.d(tag, requestStartMessage)

            //var responseMessage = ("--> ${request.method} ${request.url}${if (connection != null) " " + connection.protocol() else ""}")
            if (originalResponse.body != null) {
                Log.d(tag, originalResponse.body.toString())
            }
        }

        return originalResponse.newBuilder().code(statusCode)
            .body(bodyString?.toResponseBody(contentType = contentType)).build()
    }
}





