package io.rapidz.assignment1.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.rapidz.assignment1.data.Answer
import kotlinx.coroutines.flow.Flow

@Dao
interface AnswerDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAnswer(answer: Answer)

	@Update
	suspend fun updateAnswer(answer: Answer)

	@Query("SELECT * FROM answers")
	fun getAllAnswers(): Flow<List<Answer>>

	@Query("SELECT * FROM answers WHERE candidateId = :candidateId")
	fun getAnswersByCandidate(candidateId : Long) : Flow<List<Answer>>

	@Query("DELETE FROM answers WHERE candidateId = :candidateId")
	suspend fun deleteAnswersByCandidate(candidateId: Long)

	@Query("SELECT SUM(totalTime) FROM answers WHERE candidateId = :candidateId")
	suspend fun getTotalTimeTaken(candidateId: Long): Int

	@Query("UPDATE answers SET score = :adminScore WHERE questionId = :questionId AND candidateId = :candidateId")
	suspend fun updateAdminScore(questionId: Int, candidateId: Long, adminScore: Int)

	@Query("SELECT remainingTime FROM answers WHERE candidateId = :candidateId ORDER BY id DESC LIMIT 1")
	suspend fun getRemainingTime(candidateId: Long): Int

	@Query("UPDATE answers SET remainingTime = :remainingTime WHERE candidateId = :candidateId")
	suspend fun updateRemainingTime(candidateId: Long, remainingTime: Int)

}