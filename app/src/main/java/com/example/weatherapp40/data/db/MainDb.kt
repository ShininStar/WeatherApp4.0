package com.example.weatherapp40.data.db

import android.content.Context
import androidx.room.*
import com.example.weatherapp40.data.WeatherModel

//класс базы даннх Room

@Database(entities = [WeatherModel::class], version = 1)
@TypeConverters(MyTypeConverter::class)
abstract class MainDb : RoomDatabase() {

    abstract fun getDao(): Dao

    companion object {
        private var mainDb: MainDb ?= null

        @Synchronized
        fun getDb(context: Context) : MainDb {
            return if(mainDb == null){
                mainDb = Room.databaseBuilder(context.applicationContext, MainDb::class.java, "data.db").build()
                mainDb as MainDb
            }
            else mainDb as MainDb
        }
    }
}