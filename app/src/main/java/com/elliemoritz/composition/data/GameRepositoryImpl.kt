package com.elliemoritz.composition.data

import com.elliemoritz.composition.domain.entities.Difficulty
import com.elliemoritz.composition.domain.entities.GameSettings
import com.elliemoritz.composition.domain.entities.Question
import com.elliemoritz.composition.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min

object GameRepositoryImpl: GameRepository {

    private const val MIN_SUM = 2
    private const val MIN_ANSWER_VALUE = 1

    override fun generateQuestion(maxSumValue: Int, optionsCount: Int): Question {
        val sum = (MIN_SUM..maxSumValue).random()
        val visibleNumber = (MIN_ANSWER_VALUE until sum).random()

        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)

        val from = max(rightAnswer - optionsCount, MIN_ANSWER_VALUE)
        val to = min(rightAnswer + optionsCount, maxSumValue)

        while (options.size < optionsCount) {
            val wrongAnswer = (from..to).random()
            options.add(wrongAnswer)
        }

        return Question(
            sum,
            visibleNumber,
            options.toList()
        )
    }

    override fun getGameSettings(difficulty: Difficulty): GameSettings {
        return when (difficulty) {

            Difficulty.TEST -> {
                GameSettings(
                    10,
                    3,
                    50,
                    10
                )
            }

            Difficulty.EASY -> {
                GameSettings(
                    20,
                    10,
                    70,
                    60
                )
            }

            Difficulty.MEDIUM -> {
                GameSettings(
                    50,
                    10,
                    80,
                    45
                )
            }

            Difficulty.HARD -> {
                GameSettings(
                    100,
                    10,
                    90,
                    30
                )
            }
        }
    }
}