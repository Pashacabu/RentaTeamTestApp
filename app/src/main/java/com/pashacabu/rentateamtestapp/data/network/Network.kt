package com.pashacabu.rentateamtestapp.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.Maybe
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

class Network {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    private val inter = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val contentType = "application/json".toMediaType()

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(inter)
        .retryOnConnectionFailure(false)
        .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(json.asConverterFactory(contentType))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val apiService: NetworkInterface = retrofit.create()
    val rxService: RXJavaNetworkInterface = retrofit.create()

    interface NetworkInterface {

        @GET("users")
        suspend fun getUsers(
            @Query("page") page: Int
        ): NetworkResponse


    }

    interface RXJavaNetworkInterface {
        @GET("users")
        fun getUsers(
            @Query("page") page: Int
        ): Maybe<NetworkResponse>

    }

    companion object {
        const val baseUrl = "https://reqres.in/api/"
    }
}