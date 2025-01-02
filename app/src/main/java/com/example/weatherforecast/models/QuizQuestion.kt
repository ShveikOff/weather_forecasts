package com.example.weatherforecast.models

data class QuizQuestion(
    val text: String,
    val options: List<String>? = null,
    val answerType: AnswerType = AnswerType.RADIO
) {
    enum class AnswerType {
        RADIO, TEXT
    }
}
