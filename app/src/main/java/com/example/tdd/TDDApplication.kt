package com.example.tdd

import android.app.Application
import com.example.tdd.model.RoomDatabaseBuilder

class TDDApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        RoomDatabaseBuilder.createDatabase(this)
    }
}