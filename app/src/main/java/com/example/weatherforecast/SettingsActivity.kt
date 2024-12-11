package com.example.weatherforecast

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    private lateinit var spinnerTemperature: Spinner
    private lateinit var spinnerWindSpeed: Spinner
    private lateinit var spinnerPressure: Spinner
    private lateinit var switchWeatherAlerts: Switch
    private lateinit var switchDailyRecommendations: Switch
    private lateinit var switchUpdateReminders: Switch
    private lateinit var seekBarUpdateFrequency: SeekBar
    private lateinit var textUpdateFrequency: TextView
    private lateinit var switchDarkMode: Switch
    private lateinit var spinnerTheme: Spinner
    private lateinit var spinnerLanguage: Spinner
    private lateinit var buttonClearCache: Button
    private lateinit var buttonResetSettings: Button
    private lateinit var buttonSaveChanges: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Инициализация UI-элементов
        spinnerTemperature = findViewById(R.id.spinnerTemperature)
        spinnerWindSpeed = findViewById(R.id.spinnerWindSpeed)
        spinnerPressure = findViewById(R.id.spinnerPressure)
        switchWeatherAlerts = findViewById(R.id.switchWeatherAlerts)
        switchDailyRecommendations = findViewById(R.id.switchDailyRecommendations)
        switchUpdateReminders = findViewById(R.id.switchUpdateReminders)
        seekBarUpdateFrequency = findViewById(R.id.seekBarUpdateFrequency)
        textUpdateFrequency = findViewById(R.id.textUpdateFrequency)
        switchDarkMode = findViewById(R.id.switchDarkMode)
        spinnerTheme = findViewById(R.id.spinnerTheme)
        spinnerLanguage = findViewById(R.id.spinnerLanguage)
        buttonClearCache = findViewById(R.id.buttonClearCache)
        buttonResetSettings = findViewById(R.id.buttonResetSettings)
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges)

        setupSpinners()
        setupSeekBar()
        setupDarkModeSwitch()

        buttonClearCache.setOnClickListener {
            clearCache()
        }

        buttonResetSettings.setOnClickListener {
            resetSettings()
        }

        buttonSaveChanges.setOnClickListener {
            saveChanges()
        }
    }

    private fun setupSpinners() {
        // Установка адаптеров для спиннеров
        val unitsAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.units_array,
            android.R.layout.simple_spinner_item
        )
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTemperature.adapter = unitsAdapter
        spinnerWindSpeed.adapter = unitsAdapter
        spinnerPressure.adapter = unitsAdapter

        val themeAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.theme_array,
            android.R.layout.simple_spinner_item
        )
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTheme.adapter = themeAdapter

        val languageAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.language_array,
            android.R.layout.simple_spinner_item
        )
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = languageAdapter
    }

    private fun setupSeekBar() {
        // Установка обработчика изменения значения ползунка
        seekBarUpdateFrequency.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val hours = progress + 1
                textUpdateFrequency.text = "$hours Hour${if (hours > 1) "s" else ""}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupDarkModeSwitch() {
        // Обработчик переключения темного режима
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    private fun clearCache() {
        Toast.makeText(this, "Cache cleared", Toast.LENGTH_SHORT).show()
        // Логика для очистки кэша
    }

    private fun resetSettings() {
        Toast.makeText(this, "Settings reset", Toast.LENGTH_SHORT).show()
        // Логика для сброса настроек
    }

    private fun saveChanges() {
        Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()
        // Логика для сохранения настроек
    }
}
