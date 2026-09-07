package com.moctestgpsc.app.data.repository

import com.moctestgpsc.app.data.local.dao.TestDao
import com.moctestgpsc.app.data.local.dao.QuestionDao
import com.moctestgpsc.app.data.local.dao.UserAnswerDao
import com.moctestgpsc.app.data.local.dao.TestResultDao
import com.moctestgpsc.app.data.local.entity.TestEntity
import com.moctestgpsc.app.data.local.entity.QuestionEntity
import com.moctestgpsc.app.data.local.entity.UserAnswerEntity
import com.moctestgpsc.app.data.local.entity.TestResultEntity
import com.moctestgpsc.app.data.remote.ApiService
import com.moctestgpsc.app.data.remote.dto.QuestionDto
import com.moctestgpsc.app.data.remote.dto.SubmitAnswerDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestRepository(
    private val apiService: ApiService,
    private val testDao: TestDao,
    private val questionDao: QuestionDao,
    private val userAnswerDao: UserAnswerDao,
    private val testResultDao: TestResultDao
) {

    // Test operations
    fun getAllTests(): Flow<List<TestEntity>> = testDao.getAllTests()

    fun getTestById(testId: Int): Flow<TestEntity> = testDao.getTestById(testId)

    suspend fun refreshTests() = flow {
        try {
            val response = apiService.getAllTests()
            if (response.isSuccessful) {
                response.body()?.forEach { testDto ->
                    val testEntity = TestEntity(
                        id = testDto.id,
                        title = testDto.title,
                        description = testDto.description,
                        duration = testDto.duration,
                        totalQuestions = testDto.totalQuestions,
                        passingScore = testDto.passingScore,
                        createdAt = testDto.createdAt
                    )
                    testDao.insertTest(testEntity)
                }
                emit(true)
            } else {
                emit(false)
            }
        } catch (e: Exception) {
            emit(false)
        }
    }

    // Question operations
    fun getQuestionsByTestId(testId: Int): Flow<List<QuestionEntity>> = 
        questionDao.getQuestionsByTestId(testId)

    fun getQuestionById(questionId: Int): Flow<QuestionEntity> = 
        questionDao.getQuestionById(questionId)

    suspend fun fetchTestQuestions(testId: Int) = flow {
        try {
            val response = apiService.getTestQuestions(testId)
            if (response.isSuccessful) {
                response.body()?.let { questions ->
                    val questionEntities = questions.map { dto ->
                        QuestionEntity(
                            id = dto.id,
                            testId = dto.testId,
                            questionNumber = dto.questionNumber,
                            questionText = dto.questionText,
                            options = dto.options.joinToString(","),
                            correctAnswer = dto.correctAnswer,
                            explanation = dto.explanation,
                            category = dto.category
                        )
                    }
                    questionDao.insertQuestions(questionEntities)
                }
                emit(true)
            } else {
                emit(false)
            }
        } catch (e: Exception) {
            emit(false)
        }
    }

    // User answer operations
    suspend fun saveUserAnswer(answer: UserAnswerEntity) {
        userAnswerDao.insertAnswer(answer)
    }

    fun getUserAnswersByTestId(testId: Int): Flow<List<UserAnswerEntity>> = 
        userAnswerDao.getAnswersByTestId(testId)

    fun getUserAnswer(testId: Int, questionId: Int): Flow<UserAnswerEntity?> = 
        userAnswerDao.getAnswerByTestAndQuestion(testId, questionId)

    suspend fun submitAnswer(submitAnswerDto: SubmitAnswerDto) = flow {
        try {
            val response = apiService.submitAnswer(submitAnswerDto)
            emit(response.isSuccessful)
        } catch (e: Exception) {
            emit(false)
        }
    }

    // Test result operations
    suspend fun saveTestResult(result: TestResultEntity) {
        testResultDao.insertResult(result)
    }

    fun getAllResults(): Flow<List<TestResultEntity>> = testResultDao.getAllResults()

    fun getResultsByTestId(testId: Int): Flow<List<TestResultEntity>> = 
        testResultDao.getResultsByTestId(testId)

    fun getAverageScore(testId: Int): Flow<Double?> = 
        testResultDao.getAverageScoreForTest(testId)
}