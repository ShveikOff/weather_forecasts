package com.example.weatherforecast

class SettingsScreen {

    // Поля экрана настроек
    private var nameField: String = ""                  // Поле для ввода имени
    private var degreesChoose: String = "Celsius"       // Единицы температуры
    private var windChoose: String = "km/h"             // Единицы скорости ветра
    private var osadkiChoose: String = "mm"             // Единицы осадков
    private var pressure: String = "hPa"               // Единицы давления
    private var distance: String = "km"                // Единицы расстояния
    private var changeMode: Boolean = false            // Режим (например, темный/светлый)
    private var notificationsChoose: String = "Enabled" // Уведомления
    private var nightUpdate: Boolean = false           // Ночные обновления
    private var languageChoose: String = "English"     // Язык интерфейса

    // Методы для обработки событий
    fun degreesChooseEvent(value: String) {
        degreesChoose = value
        println("Temperature unit changed to: $degreesChoose")
    }

    fun windChooseEvent(value: String) {
        windChoose = value
        println("Wind unit changed to: $windChoose")
    }

    fun osadkiChooseEvent(value: String) {
        osadkiChoose = value
        println("Precipitation unit changed to: $osadkiChoose")
    }

    fun pressureChooseEvent(value: String) {
        pressure = value
        println("Pressure unit changed to: $pressure")
    }

    fun distanceChooseEvent(value: String) {
        distance = value
        println("Distance unit changed to: $distance")
    }

    fun changeModeEvent(isDarkMode: Boolean) {
        changeMode = isDarkMode
        val mode = if (changeMode) "Dark Mode" else "Light Mode"
        println("Display mode changed to: $mode")
    }

    fun notificationsChooseEvent(value: String) {
        notificationsChoose = value
        println("Notifications setting changed to: $notificationsChoose")
    }

    fun nightUpdateEvent(isEnabled: Boolean) {
        nightUpdate = isEnabled
        println("Night updates set to: ${if (nightUpdate) "Enabled" else "Disabled"}")
    }

    fun languageChooseEvent(value: String) {
        languageChoose = value
        println("Language changed to: $languageChoose")
    }

    // Применение настроек
    fun applySettings() {
        println("Applying settings...")
        println("Name: $nameField")
        println("Degrees: $degreesChoose")
        println("Wind: $windChoose")
        println("Precipitation: $osadkiChoose")
        println("Pressure: $pressure")
        println("Distance: $distance")
        println("Mode: ${if (changeMode) "Dark" else "Light"}")
        println("Notifications: $notificationsChoose")
        println("Night Updates: ${if (nightUpdate) "Enabled" else "Disabled"}")
        println("Language: $languageChoose")
        println("Settings applied successfully!")
    }
}

fun main() {
    // Пример использования
    val settingsScreen = SettingsScreen()

    // Эмуляция изменения настроек
    settingsScreen.degreesChooseEvent("Fahrenheit")
    settingsScreen.windChooseEvent("m/s")
    settingsScreen.osadkiChooseEvent("inches")
    settingsScreen.pressureChooseEvent("mmHg")
    settingsScreen.distanceChooseEvent("miles")
    settingsScreen.changeModeEvent(true)
    settingsScreen.notificationsChooseEvent("Disabled")
    settingsScreen.nightUpdateEvent(true)
    settingsScreen.languageChooseEvent("French")

    // Применение настроек
    settingsScreen.applySettings()
}
