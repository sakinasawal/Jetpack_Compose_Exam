package io.rapidz.assignment1.repository

import io.rapidz.assignment1.dao.AppDao
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.Candidate
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

	suspend fun updateAnswer(answer: Answer){
		appDao.updateAnswer(answer)
	}

	fun getAnswers(): Flow<List<Answer>> {
		return appDao.getAllAnswers()
	}

	fun getAnswersByCandidate(candidateId : Long) : Flow<List<Answer>> {
		return appDao.getAnswersByCandidate(candidateId)
	}

	suspend fun deleteAnswersForCandidate(candidateId: Long) {
		appDao.deleteAnswersByCandidate(candidateId)
	}

	suspend fun getTotalTimeTaken(candidateId: Long): Int {
		return appDao.getTotalTimeTaken(candidateId)
	}

	suspend fun updateAdminScore(questionId: Int, candidateId: Long, adminScore: Int) {
		appDao.updateAdminScore(questionId, candidateId, adminScore)
	}

	suspend fun getRemainingTimeForCandidate(candidateId: Long): Int {
		return appDao.getRemainingTime(candidateId)
	}

	suspend fun updateRemainingTime(candidateId: Long, remainingTime: Int) {
		appDao.updateRemainingTime(candidateId, remainingTime)
	}

	// end region
}