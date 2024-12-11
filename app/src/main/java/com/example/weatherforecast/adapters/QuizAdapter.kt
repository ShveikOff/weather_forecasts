package com.example.weatherforecast.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.ItemQuizQuestionBinding
import com.example.weatherforecast.models.QuizQuestion

class QuizAdapter(private val questions: List<QuizQuestion>) :
    RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    private val answers = mutableMapOf<Int, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemQuizQuestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
        holder.binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedAnswer = holder.binding.radioGroup.findViewById<RadioButton>(checkedId).text.toString()
            answers[position] = selectedAnswer
        }
        holder.binding.answerEditText.setOnFocusChangeListener { _, _ ->
            answers[position] = holder.binding.answerEditText.text.toString()
        }
    }

    override fun getItemCount(): Int = questions.size

    fun getAnswers(): Map<Int, String> = answers

    class QuizViewHolder(val binding: ItemQuizQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: QuizQuestion) {
            binding.questionText.text = question.text
            binding.radioGroup.removeAllViews()
            question.options?.forEach { option ->
                val radioButton = RadioButton(binding.root.context)
                radioButton.text = option
                binding.radioGroup.addView(radioButton)
            }
            binding.answerEditText.visibility =
                if (question.answerType == QuizQuestion.AnswerType.TEXT) View.VISIBLE else View.GONE
        }
    }
}
