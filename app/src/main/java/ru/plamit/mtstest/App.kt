package ru.plamit.mtstest

import android.app.Application
import org.koin.android.ext.android.startKoin
import ru.plamit.mtstest.di.usersListModule

class App: Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin(this, arrayListOf(usersListModule))
    }
}