package com.example.learningroomdatabase

import android.app.Application
import com.example.learningroomdatabase.data.MainDataBase

class BaseApplication : Application() {
    val dataBase by lazy{
        MainDataBase.createDataBase(this)
    }
}