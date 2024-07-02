package io.rapidz.assignment1.repository

import io.rapidz.assignment1.dao.AnswerDao
import io.rapidz.assignment1.dao.CandidateDao
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.Candidate
import javax.inject.Inject

class CandidateRepository @Inject constructor (
	private val candidateDao : CandidateDao? = null,
	private val answerDao : AnswerDao? = null
) {

	suspend fun insertCandidate(candidate : Candidate){
		candidateDao!!.insert(candidate)
	}

	suspend fun saveAnswer(answer: Answer) {
		answerDao?.insert(answer)
	}

	suspend fun getAnswersForCandidate(candidateId: Long): List<Answer> {
		return answerDao!!.getAnswersForCandidate(candidateId)
	}
}