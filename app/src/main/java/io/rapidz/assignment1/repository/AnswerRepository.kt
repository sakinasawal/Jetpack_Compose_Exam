package io.rapidz.assignment1.repository

import io.rapidz.assignment1.dao.AnswerDao
import io.rapidz.assignment1.dao.CandidateDao
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.Candidate
import javax.inject.Inject

class AnswerRepository @Inject constructor (
	private val answerDao : AnswerDao
){
	suspend fun saveAnswer(answer: Answer) {
		answerDao.insert(answer)
	}

	suspend fun getAnswersForCandidate(candidateId: Long): List<Answer> {
		return answerDao.getAnswersForCandidate(candidateId)
	}
}