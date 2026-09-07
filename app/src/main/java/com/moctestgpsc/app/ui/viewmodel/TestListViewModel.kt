package com.moctestgpsc.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moctestgpsc.app.data.repository.TestRepository
import com.moctestgpsc.app.data.local.entity.TestEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TestListViewModel(
    private val repository: TestRepository
) : ViewModel() {

    private val _tests = MutableStateFlow<List<TestEntity>>(emptyList())
    val tests: StateFlow<List<TestEntity>> = _tests

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadTests()
    }

    private fun loadTests() {
        viewModelScope.launch {
            try {
                repository.getAllTests().collect { testList ->
                    _tests.value = testList
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun refreshTests() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.refreshTests().collect { success ->
                    _isLoading.value = false
                    if (!success) {
                        _error.value = "Failed to refresh tests"
                    }
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

class TestListViewModelFactory(
    private val repository: TestRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TestListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
