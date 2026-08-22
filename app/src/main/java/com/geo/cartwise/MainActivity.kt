package com.geo.cartwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.geo.cartwise.presentation.navigation.CartWiseNavHost
import com.geo.cartwise.presentation.theme.CartWiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as CartWiseApp).container

        setContent {
            CartWiseTheme {
                CartWiseNavHost(container = container)
            }
        }
    }
}
