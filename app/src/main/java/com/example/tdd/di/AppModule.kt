package com.example.tdd.di

import android.content.Context
import androidx.room.Room
import com.example.tdd.model.TDDDatabase
import com.example.tdd.model.TDDRepositoryImpl
import com.example.tdd.model.data.local.dao.TDDDao
import com.example.tdd.model.data.remote.service.TDDApiService
import com.example.tdd.viewmodels.domain.TDDRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL = "https://jsonplaceholder.typicode.com"
private const val DATABASE_NAME = "tdd_database"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTDDDatabase(@ApplicationContext context: Context): TDDDatabase =
        Room.databaseBuilder(context, TDDDatabase::class.java, DATABASE_NAME).build()

    @Provides
    fun provideTDDDao(database: TDDDatabase): TDDDao = database.tddDao()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideTDDApiService(retrofit: Retrofit): TDDApiService =
        retrofit.create(TDDApiService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTDDRepository(repository: TDDRepositoryImpl): TDDRepository
}