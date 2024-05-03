package com.elliemoritz.composition.domain.entities

data class GameResult(
    val playerWins: Boolean,
    val rightAnswersCount: Int,
    val totalQuestionsCount: Int,
    val gameSettings: GameSettings
)