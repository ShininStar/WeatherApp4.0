package com.example.weatherapp40.data.di

import android.app.Application
import androidx.room.Room
import com.example.weatherapp40.Constants
import com.example.weatherapp40.data.database.MainDb
import com.example.weatherapp40.data.database.repository.WeatherRepository
import com.example.weatherapp40.data.database.repository.WeatherRepositoryImpl
import com.example.weatherapp40.data.model.api.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMainDb(app: Application) : MainDb {
        return Room.databaseBuilder(
            app,
            MainDb::class.java,
            "data.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(api: WeatherApi, db: MainDb) : WeatherRepository {
        return WeatherRepositoryImpl(api, db.dao)
    }
}