package io.rapidz.assignment1.repository

import androidx.room.Update
import io.rapidz.assignment1.dao.AppDao
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.Candidate
import io.rapidz.assignment1.data.CandidateWithScore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor (
	private val appDao : AppDao
) {

	// region Candidate
	// =============================================================================================================
	suspend fun insertCandidate(candidate : Candidate) : Long {
		return appDao.insert(candidate)
	}

	suspend fun getCandidateByEmail(email: String): Candidate? {
		return appDao.getCandidateByEmail(email)
	}

	suspend fun getAllCandidates(): List<Candidate> {
		return appDao.getAllCandidates()
	}

	// end region
	
	// region Test
	// =============================================================================================================

	suspend fun saveAnswer(answer: Answer) {
		appDao.insertAnswer(answer)
	}

	fun getAnswersByCandidate(candidateId : Long) : Flow<List<Answer>> {
		return appDao.getAnswersByCandidate(candidateId)
	}

	suspend fun deleteAnswersForCandidate(candidateId: Long) {
		appDao.deleteAnswersByCandidate(candidateId)
	}

	suspend fun updateAnswer(answer: Answer){
		appDao.updateAnswer(answer)
	}

	// end region
}