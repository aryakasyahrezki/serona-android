package com.example.serona.di

import com.example.serona.data.api.UserApi
import com.example.serona.data.api.TutorialApi
import com.example.serona.data.network.AuthInterceptor
import com.example.serona.data.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{

    private const val BASE_URL = "https://serona.azurewebsites.net/"
    @Provides
    @Singleton
    fun provideOkHttp(
        authRepo: AuthRepository
    ): OkHttpClient{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(authRepo))
            .addInterceptor(logging)
            // ini untuk nyoba dlu karena database nya lama bgt
            .connectTimeout(60, TimeUnit.SECONDS) // Waktu maksimal untuk menyambung ke server
            .readTimeout(60, TimeUnit.SECONDS)    // Waktu maksimal untuk membaca respon
            .writeTimeout(60, TimeUnit.SECONDS)   // Waktu maksimal untuk mengirim data

//            .followRedirects(true)
//            .followSslRedirects(true)
//            .addInterceptor(logging)

            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi{
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTutorialApi(retrofit: Retrofit): TutorialApi{
        return retrofit.create(TutorialApi::class.java)
    }
}