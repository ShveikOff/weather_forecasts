package com.example.weatherforecast

class DatabaseManager(private val dbName: String) {
    private val connection: Any? = connectToDatabase()

    private fun connectToDatabase(): Any? {
        // Подключение к базе данных (имитация)
        println("Connecting to database $dbName")
        return null // Здесь может быть объект соединения
    }

    fun saveUserQuiz(userQuizScreen: UserQuizScreen) {
        // Сохранение пользовательских настроек, полученных от UserQuizScreen
        val quiz = userQuizScreen.getUserQuizScreen()  // Исправлена опечатка
        println("Saving quizes: $quiz")
        // Логика для сохранения настроек в базу данных
    }

    fun saveNotificationSettings(notifications: Notifications) {
        // Сохранение настроек уведомлений, полученных от Notifications
        val settings = notifications.getNotificationSettings()
        println("Saving notification settings: $settings")
        // Логика для сохранения настроек в базу данных
    }

    fun saveApiData(apiManager: APIManager) {
        // Сохранение данных, полученных от APIManager
        val apiData = apiManager.getData()
        println("Saving API data: $apiData")
        // Логика для сохранения данных в базу данных
    }

    fun saveForecastMap(forecastMapScreen: ForecastMapScreen) {
        // Сохранение информации о прогнозе, полученной от ForecastMapScreen
        val forecastInfo = forecastMapScreen.getForecastMap()
        println("Saving forecast information: $forecastInfo")
        // Логика для сохранения данных в базу данных
    }

    fun saveRecommendations(recommendationScreen: RecommendationScreen) {
        // Сохранение рекомендаций, полученных от RecommendationScreen
        val recommendations = recommendationScreen.getRecommendations()
        println("Saving recommendations: $recommendations")
        // Логика для сохранения данных в базу данных
    }

    fun saveCityChooseData(cityChooseScreen: CityChooseScreen) {
        // Сохранение данных о выбранном городе, полученных от CityChooseScreen
        val chosenCity = cityChooseScreen.getChosenCity()
        println("Saving chosen city: $chosenCity")
        // Логика для сохранения выбранного города в базу данных
    }

    fun saveSettings(settingsScreen: SettingsScreen) {
        // Сохранение настроек, полученных от SettingsScreen
        val settings = settingsScreen.getSettings()
        println("Saving settings: $settings")
        // Логика для сохранения настроек в базу данных
    }

    fun closeConnection() {
        // Закрытие соединения с базой данных
        println("Closing database connection")
        // Логика закрытия соединения
    }
}

// Пример взаимодействия
fun main() {
    // Создаем экземпляры классов, чтобы продемонстрировать взаимодействие
    val mainScreen = MainActivity()
    val userQuizScreen = UserQuizScreen()
    val notifications = Notifications()
    val apiManager = APIManager()
    val forecastMapScreen = ForecastMapScreen()
    val recommendationScreen = RecommendationScreen()
    val cityChooseScreen = CityChooseScreen()  // Исправлено имя переменной
    val settingsScreen = SettingsScreen()

    // Создаем экземпляр DatabaseManager и взаимодействуем с другими классами
    val dbManager = DatabaseManager("my_database")
    dbManager.saveUserQuiz(userQuizScreen)
    dbManager.saveNotificationSettings(notifications)
    dbManager.saveApiData(apiManager)
    dbManager.saveForecastMap(forecastMapScreen)
    dbManager.saveRecommendations(recommendationScreen)
    dbManager.saveCityChooseData(cityChooseScreen)
    dbManager.saveSettings(settingsScreen)
    dbManager.closeConnection()
}
