package com.example.weatherforecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Подключение разметки activity_login.xml
        setContentView(R.layout.activity_main)
    }
}
