package com.moctestgpsc.app.ui.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.moctestgpsc.app.data.local.MoctestDatabase
import com.moctestgpsc.app.data.repository.TestRepository
import com.moctestgpsc.app.databinding.ActivityTestBinding
import com.moctestgpsc.app.ui.viewmodel.TestViewModel
import com.moctestgpsc.app.ui.viewmodel.TestViewModelFactory
import kotlinx.coroutines.launch

class TestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestBinding
    private lateinit var viewModel: TestViewModel
    private var testId: Int = -1
    private var countDownTimer: CountDownTimer? = null
    private var timeSpentOnQuestion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        testId = intent.getIntExtra("testId", -1)
        if (testId == -1) {
            finish()
            return
        }

        setupViewModel()
        setupObservers()
        setupListeners()
    }

    private fun setupViewModel() {
        val database = MoctestDatabase.getDatabase(this)
        val repository = TestRepository(
            apiService = ApiClientProvider.getApiService(),
            testDao = database.testDao(),
            questionDao = database.questionDao(),
            userAnswerDao = database.userAnswerDao(),
            testResultDao = database.testResultDao()
        )
        val factory = TestViewModelFactory(repository, testId)
        viewModel = ViewModelProvider(this, factory).get(TestViewModel::class.java)
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.questions.collect { questions ->
                if (questions.isNotEmpty()) {
                    displayQuestion()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.currentQuestionIndex.collect { index ->
                displayQuestion()
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnPrevious.setOnClickListener {
            viewModel.moveToPreviousQuestion()
            startQuestionTimer()
        }

        binding.btnNext.setOnClickListener {
            saveAnswer()
            viewModel.moveToNextQuestion()
            startQuestionTimer()
        }

        binding.btnSubmit.setOnClickListener {
            submitTest()
        }
    }

    private fun displayQuestion() {
        val question = viewModel.getCurrentQuestion()
        if (question != null) {
            binding.questionText.text = question.questionText
            binding.questionNumber.text = "Question ${question.questionNumber}"

            val options = question.options.split(",")
            setupOptionsGroup(options)

            startQuestionTimer()
        }
    }

    private fun setupOptionsGroup(options: List<String>) {
        binding.optionsGroup.removeAllViews()
        options.forEachIndexed { index, option ->
            val radioButton = android.widget.RadioButton(this).apply {
                text = option
                id = View.generateViewId()
            }
            binding.optionsGroup.addView(radioButton)
        }
    }

    private fun startQuestionTimer() {
        countDownTimer?.cancel()
        timeSpentOnQuestion = 0
        // Timer implementation for tracking time spent on each question
    }

    private fun saveAnswer() {
        val question = viewModel.getCurrentQuestion()
        if (question != null) {
            val selectedOptionId = binding.optionsGroup.checkedRadioButtonId
            if (selectedOptionId != -1) {
                val selectedRadioButton = findViewById<android.widget.RadioButton>(selectedOptionId)
                val selectedAnswer = selectedRadioButton.text.toString()
                viewModel.submitAnswer(question.id, selectedAnswer, timeSpentOnQuestion)
            }
        }
    }

    private fun submitTest() {
        saveAnswer()
        // TODO: Calculate results and navigate to results activity
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
