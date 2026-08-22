package com.geo.cartwise

import android.app.Application
import com.geo.cartwise.di.AppContainer

class CartWiseApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
