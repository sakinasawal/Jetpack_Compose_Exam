package io.rapidz.assignment1.repository

import io.rapidz.assignment1.dao.CandidateDao
import io.rapidz.assignment1.data.Candidate
import javax.inject.Inject

class CandidateRepository @Inject constructor (
	private val candidateDao : CandidateDao
) {
	suspend fun insertCandidate(candidate : Candidate){
		candidateDao.insert(candidate)
	}
}