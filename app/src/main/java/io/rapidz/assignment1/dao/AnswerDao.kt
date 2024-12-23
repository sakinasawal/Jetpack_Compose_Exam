package io.rapidz.assignment1.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.rapidz.assignment1.data.Answer
import kotlinx.coroutines.flow.Flow

@Dao
interface AnswerDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAnswer(answer: Answer)

	@Query("SELECT * FROM answers")
	fun getAllAnswers(): Flow<List<Answer>>
}