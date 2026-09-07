package com.moctestgpsc.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.moctestgpsc.app.data.local.entity.TestEntity
import com.moctestgpsc.app.data.local.entity.QuestionEntity
import com.moctestgpsc.app.data.local.entity.UserAnswerEntity
import com.moctestgpsc.app.data.local.entity.TestResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TestDao {
    @Insert
    suspend fun insertTest(test: TestEntity)

    @Query("SELECT * FROM tests")
    fun getAllTests(): Flow<List<TestEntity>>

    @Query("SELECT * FROM tests WHERE id = :testId")
    fun getTestById(testId: Int): Flow<TestEntity>

    @Update
    suspend fun updateTest(test: TestEntity)

    @Query("DELETE FROM tests WHERE id = :testId")
    suspend fun deleteTest(testId: Int)
}

@Dao
interface QuestionDao {
    @Insert
    suspend fun insertQuestion(question: QuestionEntity)

    @Insert
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Query("SELECT * FROM questions WHERE testId = :testId ORDER BY questionNumber ASC")
    fun getQuestionsByTestId(testId: Int): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE id = :questionId")
    fun getQuestionById(questionId: Int): Flow<QuestionEntity>

    @Query("DELETE FROM questions WHERE testId = :testId")
    suspend fun deleteQuestionsByTestId(testId: Int)
}

@Dao
interface UserAnswerDao {
    @Insert
    suspend fun insertAnswer(answer: UserAnswerEntity)

    @Query("SELECT * FROM user_answers WHERE testId = :testId")
    fun getAnswersByTestId(testId: Int): Flow<List<UserAnswerEntity>>

    @Query("SELECT * FROM user_answers WHERE testId = :testId AND questionId = :questionId")
    fun getAnswerByTestAndQuestion(testId: Int, questionId: Int): Flow<UserAnswerEntity?>

    @Update
    suspend fun updateAnswer(answer: UserAnswerEntity)

    @Query("DELETE FROM user_answers WHERE testId = :testId")
    suspend fun deleteAnswersByTestId(testId: Int)
}

@Dao
interface TestResultDao {
    @Insert
    suspend fun insertResult(result: TestResultEntity)

    @Query("SELECT * FROM test_results ORDER BY completedAt DESC")
    fun getAllResults(): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results WHERE testId = :testId ORDER BY completedAt DESC")
    fun getResultsByTestId(testId: Int): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results WHERE id = :resultId")
    fun getResultById(resultId: Int): Flow<TestResultEntity>

    @Query("SELECT AVG(percentage) FROM test_results WHERE testId = :testId")
    fun getAverageScoreForTest(testId: Int): Flow<Double?>
}