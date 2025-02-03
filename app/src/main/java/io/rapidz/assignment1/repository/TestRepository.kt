package io.rapidz.assignment1.repository

import io.rapidz.assignment1.dao.AnswerDao
import io.rapidz.assignment1.data.Answer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TestRepository @Inject constructor(private val answerDao: AnswerDao) {

	suspend fun saveAnswer(answer: Answer) {
		answerDao.insertAnswer(answer)
	}

	suspend fun updateAnswer(answer: Answer){
		answerDao.updateAnswer(answer)
	}

	fun getAnswers(): Flow<List<Answer>> {
		return answerDao.getAllAnswers()
	}

	fun getAnswersByCandidate(candidateId : Long) : Flow<List<Answer>>{
		return answerDao.getAnswersByCandidate(candidateId)
	}

	suspend fun deleteAnswersForCandidate(candidateId: Long) {
		answerDao.deleteAnswersByCandidate(candidateId)
	}

	suspend fun getTimerForCandidate(candidateId: Long): Int? =
		answerDao.getTimerForCandidate(candidateId)

	suspend fun getTotalTimeTaken(candidateId: Long): Int {
		return answerDao.getTotalTimeTaken(candidateId)
	}
}