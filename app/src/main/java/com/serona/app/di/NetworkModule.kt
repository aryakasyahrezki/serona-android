package com.serona.app.di

import com.serona.app.data.api.UserApi
import com.serona.app.data.api.TutorialApi
import com.serona.app.data.api.FaceAnalysisApi
import com.serona.app.data.network.AuthInterceptor
import com.serona.app.data.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

// 1. Tambahkan Qualifier di sini agar tidak bentrok
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FaceAnalysisRetrofit
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(
        authRepo: AuthRepository
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(authRepo))
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    // 2. Retrofit Khusus Backend User
    @UserRetrofit
    @Provides
    @Singleton
    fun provideUserRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://serona.azurewebsites.net/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 3. Retrofit Khusus ML (Azure Container Apps)
    @FaceAnalysisRetrofit
    @Provides
    @Singleton
    fun provideFaceAnalysisRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://serona-ml.wittysmoke-32718122.southeastasia.azurecontainerapps.io/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 4. Inject masing-masing API dengan Retrofit yang sesuai
    @Provides
    @Singleton
    fun provideUserApi(@UserRetrofit retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTutorialApi(@UserRetrofit retrofit: Retrofit): TutorialApi{
        return retrofit.create(TutorialApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFaceAnalysisApi(@FaceAnalysisRetrofit retrofit: Retrofit): FaceAnalysisApi {
        return retrofit.create(FaceAnalysisApi::class.java)
    }
}