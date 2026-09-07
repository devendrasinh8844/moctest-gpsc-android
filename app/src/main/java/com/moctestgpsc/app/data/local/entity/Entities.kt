package com.moctestgpsc.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tests")
data class TestEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val duration: Int, // in minutes
    val totalQuestions: Int,
    val passingScore: Int,
    val createdAt: String,
    val isCompleted: Boolean = false,
    val score: Int? = null
)

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey
    val id: Int,
    val testId: Int,
    val questionNumber: Int,
    val questionText: String,
    val options: String, // JSON string of options list
    val correctAnswer: String,
    val explanation: String? = null,
    val category: String? = null
)

@Entity(tableName = "user_answers")
data class UserAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val testId: Int,
    val questionId: Int,
    val selectedAnswer: String,
    val isCorrect: Boolean,
    val timeSpent: Int, // in seconds
    val answeredAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val testId: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val score: Int,
    val percentage: Double,
    val duration: Long, // in milliseconds
    val completedAt: Long = System.currentTimeMillis(),
    val isPassed: Boolean
)