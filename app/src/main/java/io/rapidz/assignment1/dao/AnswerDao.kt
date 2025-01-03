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
}