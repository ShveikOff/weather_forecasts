package com.example.weatherforecast

import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.adapters.QuizAdapter
import com.example.weatherforecast.models.QuizQuestion

class UserQuizActivity : AppCompatActivity() {

    private lateinit var quizRecyclerView: RecyclerView
    private lateinit var submitButton: Button
    private lateinit var quizAdapter: QuizAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_quiz)

        // Инициализация UI
        quizRecyclerView = findViewById(R.id.quizRecyclerView)
        submitButton = findViewById(R.id.submitButton)

        // Список вопросов
        val questions = listOf(
            QuizQuestion("What is your preferred temperature unit?", listOf("°C", "°F")),
            QuizQuestion("What is your usual commute method?", listOf("Car", "Bike", "Public Transport", "Walking")),
            QuizQuestion("How often do you want weather updates?", listOf("Every hour", "Every 3 hours", "Every day")),
            QuizQuestion("Enter your home location", answerType = QuizQuestion.AnswerType.TEXT)
        )

        // Настройка адаптера
        quizAdapter = QuizAdapter(questions)
        quizRecyclerView.layoutManager = LinearLayoutManager(this)
        quizRecyclerView.adapter = quizAdapter

        // Кнопка отправки
        submitButton.setOnClickListener {
            val answers = quizAdapter.getAnswers()
            // Здесь вы можете сохранить ответы в локальную базу данных
            Toast.makeText(this, "Answers saved: $answers", Toast.LENGTH_SHORT).show()
        }

        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            // Когда пользователь завершает опрос
            setResult(RESULT_OK)
            finish()
        }

        val skipButton: Button = findViewById(R.id.skipButton)
        skipButton.setOnClickListener {
            // Когда пользователь пропускает опрос
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
