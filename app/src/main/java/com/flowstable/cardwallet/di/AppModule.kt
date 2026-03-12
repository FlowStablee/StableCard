package com.flowstable.cardwallet.di

import android.content.Context
import androidx.room.Room
import com.flowstable.cardwallet.database.AppDatabase
import com.flowstable.cardwallet.network.ApiService
import com.flowstable.cardwallet.network.AuthInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(
        client: OkHttpClient,
        moshi: Moshi,
    ): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.your-fintech-backend.com/") // TODO plug real backend URL
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "flowstable.db"
        ).build()
    }

    @Provides
    fun provideCardDao(db: AppDatabase) = db.cardDao()

    @Provides
    fun provideTransactionDao(db: AppDatabase) = db.transactionDao()
}

