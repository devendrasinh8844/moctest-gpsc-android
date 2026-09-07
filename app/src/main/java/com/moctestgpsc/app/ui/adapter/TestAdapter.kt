package com.moctestgpsc.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moctestgpsc.app.data.local.entity.TestEntity
import com.moctestgpsc.app.databinding.ItemTestBinding

class TestAdapter(
    private val onTestClick: (TestEntity) -> Unit
) : ListAdapter<TestEntity, TestAdapter.TestViewHolder>(TestDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val binding = ItemTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TestViewHolder(
        private val binding: ItemTestBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(test: TestEntity) {
            binding.testTitle.text = test.title
            binding.testDescription.text = test.description
            binding.testDuration.text = "${test.duration} min"
            binding.testQuestions.text = "${test.totalQuestions} Questions"
            binding.testScore.text = "Score: ${test.score ?: "N/A"}"

            binding.root.setOnClickListener {
                onTestClick(test)
            }
        }
    }

    class TestDiffCallback : DiffUtil.ItemCallback<TestEntity>() {
        override fun areItemsTheSame(oldItem: TestEntity, newItem: TestEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TestEntity, newItem: TestEntity) =
            oldItem == newItem
    }
}
