package io.rapidz.assignment1.repository

import io.rapidz.assignment1.dao.CandidateDao
import io.rapidz.assignment1.data.Candidate
import javax.inject.Inject

class CandidateRepository @Inject constructor (
	private val candidateDao : CandidateDao
) {
	suspend fun insertCandidate(candidate : Candidate) : Long {
		return candidateDao.insert(candidate)
	}

	suspend fun getCandidateByEmail(email: String): Candidate? {
		return candidateDao.getCandidateByEmail(email)
	}

	suspend fun getAllCandidates(): List<Candidate> {
		return candidateDao.getAllCandidates()
	}
}