package io.rapidz.assignment1.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.rapidz.assignment1.data.Answer

@Dao
interface AnswerDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAnswer(answer: Answer)

	@Query("SELECT * FROM answers WHERE questionId = :questionId")
	suspend fun getAnswer(questionId: Int): Answer?
}