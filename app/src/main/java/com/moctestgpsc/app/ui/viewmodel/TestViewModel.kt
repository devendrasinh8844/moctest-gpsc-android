package com.moctestgpsc.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moctestgpsc.app.data.repository.TestRepository
import com.moctestgpsc.app.data.local.entity.QuestionEntity
import com.moctestgpsc.app.data.local.entity.UserAnswerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TestViewModel(
    private val repository: TestRepository,
    private val testId: Int
) : ViewModel() {

    private val _questions = MutableStateFlow<List<QuestionEntity>>(emptyList())
    val questions: StateFlow<List<QuestionEntity>> = _questions

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.fetchTestQuestions(testId).collect { success ->
                    if (success) {
                        repository.getQuestionsByTestId(testId).collect { questionList ->
                            _questions.value = questionList
                            _isLoading.value = false
                        }
                    } else {
                        _error.value = "Failed to load questions"
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun moveToNextQuestion() {
        if (_currentQuestionIndex.value < _questions.value.size - 1) {
            _currentQuestionIndex.value++
        }
    }

    fun moveToPreviousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value--
        }
    }

    fun submitAnswer(questionId: Int, selectedAnswer: String, timeSpent: Int) {
        viewModelScope.launch {
            try {
                val userAnswer = UserAnswerEntity(
                    testId = testId,
                    questionId = questionId,
                    selectedAnswer = selectedAnswer,
                    isCorrect = false, // Will be determined by backend
                    timeSpent = timeSpent
                )
                repository.saveUserAnswer(userAnswer)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun getCurrentQuestion(): QuestionEntity? {
        return _questions.value.getOrNull(_currentQuestionIndex.value)
    }

    fun clearError() {
        _error.value = null
    }
}

class TestViewModelFactory(
    private val repository: TestRepository,
    private val testId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TestViewModel(repository, testId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}