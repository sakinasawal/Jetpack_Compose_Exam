package io.rapidz.assignment1.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.rapidz.assignment1.data.Candidate

@Dao
interface CandidateDao{
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
}
