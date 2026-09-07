package com.moctestgpsc.app.data.remote

import com.moctestgpsc.app.data.remote.dto.TestDto
import com.moctestgpsc.app.data.remote.dto.QuestionDto
import com.moctestgpsc.app.data.remote.dto.SubmitAnswerDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path

interface ApiService {

    @GET("tests")
    suspend fun getAllTests(): Response<List<TestDto>>

    @GET("tests/{testId}")
    suspend fun getTestById(@Path("testId") testId: Int): Response<TestDto>

    @GET("tests/{testId}/questions")
    suspend fun getTestQuestions(@Path("testId") testId: Int): Response<List<QuestionDto>>

    @GET("questions/{questionId}")
    suspend fun getQuestion(@Path("questionId") questionId: Int): Response<QuestionDto>

    @POST("submit-answer")
    suspend fun submitAnswer(@Body answer: SubmitAnswerDto): Response<Map<String, Any>>

}