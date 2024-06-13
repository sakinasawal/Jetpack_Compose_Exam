package io.rapidz.assignment1.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.rapidz.assignment1.data.Candidate


@Dao
interface CandidateDao{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(candidate: Candidate)

	@Query("SELECT * FROM candidates WHERE id = :id")
	suspend fun getCandidateById(id: Int): Candidate?

	@Query("SELECT * FROM candidates WHERE emailAddress = :emailAddress LIMIT 1")
	suspend fun getCandidateByEmail(emailAddress: String): Candidate?

}
