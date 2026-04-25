package com.quizapp.data.local.database

import com.quizapp.data.local.dao.QuestionDao
import com.quizapp.data.local.entity.QuestionEntity
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val questionDao: QuestionDao
) {

    suspend fun seedIfEmpty() {
        val count = questionDao.getQuestionCount()
        if (count == 0) {
            questionDao.insertAll(grammarQuestions)
        }
    }

    private val grammarQuestions = listOf(
        QuestionEntity(
            id = 1,
            questionText = "Which sentence uses the correct subject-verb agreement?",
            optionA = "The team are playing well today.",
            optionB = "The team is playing well today.",
            optionC = "The team were playing well today.",
            optionD = "The team have playing well today.",
            correctOption = "B",
            explanation = "Collective nouns like 'team' take a singular verb in American English. 'The team is' is grammatically correct.",
            category = "Grammar"
        ),
        QuestionEntity(
            id = 2,
            questionText = "Choose the sentence with the correct use of a comma:",
            optionA = "I wanted to go to the store, but, I forgot my wallet.",
            optionB = "I wanted to go to the store but I forgot my wallet.",
            optionC = "I wanted to go to the store, but I forgot my wallet.",
            optionD = "I wanted, to go to the store but I forgot my wallet.",
            correctOption = "C",
            explanation = "When joining two independent clauses with a coordinating conjunction (but), a comma is placed before the conjunction.",
            category = "Grammar"
        ),
        QuestionEntity(
            id = 3,
            questionText = "Select the correct form of the verb: 'Neither the students nor the teacher ___ ready.'",
            optionA = "were",
            optionB = "are",
            optionC = "was",
            optionD = "is",
            correctOption = "D",
            explanation = "With 'neither...nor', the verb agrees with the subject closest to it. 'Teacher' is singular, so 'is' is correct.",
            category = "Grammar"
        ),
        QuestionEntity(
            id = 4,
            questionText = "Which word correctly completes this sentence? 'She has ___ the report already.'",
            optionA = "wrote",
            optionB = "written",
            optionC = "write",
            optionD = "writing",
            correctOption = "B",
            explanation = "The present perfect tense (has + past participle) requires 'written', the past participle of 'write'.",
            category = "Grammar"
        ),
        QuestionEntity(
            id = 5,
            questionText = "Which sentence contains a dangling modifier?",
            optionA = "Running quickly, she caught the bus on time.",
            optionB = "Running quickly, the bus was caught by her.",
            optionC = "She caught the bus while running quickly.",
            optionD = "The bus was caught by her after she ran quickly.",
            correctOption = "B",
            explanation = "In option B, 'Running quickly' dangles because it incorrectly modifies 'the bus' instead of the person running.",
            category = "Grammar"
        )
    )
}