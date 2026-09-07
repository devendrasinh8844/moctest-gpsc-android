package com.moctestgpsc.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TestDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("duration")
    val duration: Int, // in minutes
    @SerializedName("totalQuestions")
    val totalQuestions: Int,
    @SerializedName("passingScore")
    val passingScore: Int,
    @SerializedName("createdAt")
    val createdAt: String
)

data class QuestionDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("testId")
    val testId: Int,
    @SerializedName("questionNumber")
    val questionNumber: Int,
    @SerializedName("questionText")
    val questionText: String,
    @SerializedName("options")
    val options: List<String>,
    @SerializedName("correctAnswer")
    val correctAnswer: String,
    @SerializedName("explanation")
    val explanation: String? = null,
    @SerializedName("category")
    val category: String? = null
)

data class SubmitAnswerDto(
    @SerializedName("testId")
    val testId: Int,
    @SerializedName("questionId")
    val questionId: Int,
    @SerializedName("selectedAnswer")
    val selectedAnswer: String,
    @SerializedName("timeSpent")
    val timeSpent: Int // in seconds
)