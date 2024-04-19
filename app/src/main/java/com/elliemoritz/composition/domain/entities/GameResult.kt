package com.elliemoritz.composition.domain.entities

import java.io.Serializable

data class GameResult(
    val playerIsWinner: Boolean,
    val rightAnswersCount: Int,
    val totalQuestionsCount: Int,
    val gameSettings: GameSettings
) : Serializable