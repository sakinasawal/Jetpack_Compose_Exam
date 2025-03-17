package io.rapidz.assignment1.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.Candidate
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao{

	// region Candidate
	// =============================================================================================================

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(candidate: Candidate) : Long

	@Query("SELECT * FROM candidates WHERE emailAddress = :email LIMIT 1")
	suspend fun getCandidateByEmail(email: String): Candidate?

	@Query("SELECT * FROM candidates")
	suspend fun getAllCandidates(): List<Candidate>

	@Query("SELECT * FROM candidates WHERE id = :candidateId LIMIT 1")
	suspend fun getCandidateById(candidateId: Long): Candidate?

	@Update
	suspend fun updateCandidate(candidate: Candidate)

	// end region

	// region Test
	// =============================================================================================================

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAnswer(answer: Answer)

	@Query("SELECT * FROM answers WHERE candidateId = :candidateId")
	fun getAnswersByCandidate(candidateId : Long) : Flow<List<Answer>>

	@Query("DELETE FROM answers WHERE candidateId = :candidateId")
	suspend fun deleteAnswersByCandidate(candidateId: Long)

	@Update
	suspend fun updateAnswer(answer: Answer)

	// end region
}
