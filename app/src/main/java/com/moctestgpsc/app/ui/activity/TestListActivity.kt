package com.moctestgpsc.app.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.moctestgpsc.app.data.local.MoctestDatabase
import com.moctestgpsc.app.data.repository.TestRepository
import com.moctestgpsc.app.databinding.ActivityTestListBinding
import com.moctestgpsc.app.ui.adapter.TestAdapter
import com.moctestgpsc.app.ui.viewmodel.TestListViewModel
import com.moctestgpsc.app.ui.viewmodel.TestListViewModelFactory
import kotlinx.coroutines.launch

class TestListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestListBinding
    private lateinit var viewModel: TestListViewModel
    private lateinit var testAdapter: TestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
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
        val factory = TestListViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(TestListViewModel::class.java)
    }

    private fun setupRecyclerView() {
        testAdapter = TestAdapter { test ->
            // Navigate to test activity
            startTestActivity(test.id)
        }
        binding.testsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TestListActivity)
            adapter = testAdapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.tests.collect { tests ->
                testAdapter.submitList(tests)
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) {
                    android.view.View.VISIBLE
                } else {
                    android.view.View.GONE
                }
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { error ->
                if (error != null) {
                    android.widget.Toast.makeText(
                        this@TestListActivity,
                        error,
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    viewModel.clearError()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshTests()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun startTestActivity(testId: Int) {
        val intent = android.content.Intent(this, TestActivity::class.java)
        intent.putExtra("testId", testId)
        startActivity(intent)
    }
}
