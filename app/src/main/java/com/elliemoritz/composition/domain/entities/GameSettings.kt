package com.elliemoritz.composition.domain.entities

data class GameSettings(
    val maxSumValue: Int,
    val minRightAnswersCount: Int,
    val minRightAnswersPercentage: Int,
    val gameTimeInSeconds: Int
)