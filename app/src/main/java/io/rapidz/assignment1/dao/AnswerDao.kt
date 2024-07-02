package io.rapidz.assignment1.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.rapidz.assignment1.data.Answer

@Dao
interface AnswerDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(answer: Answer)

	@Query("SELECT * FROM Answer WHERE candidateId = :candidateId")
	suspend fun getAnswersForCandidate(candidateId: Long): List<Answer>
}